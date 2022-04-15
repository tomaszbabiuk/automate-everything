/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.zigbee2mqttplugin

import com.google.gson.Gson
import eu.automateeverything.data.Repository
import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.domain.mqtt.MqttListener
import eu.automateeverything.zigbee2mqttplugin.data.*
import eu.automateeverything.zigbee2mqttplugin.ports.*
import kotlinx.coroutines.*
import java.net.InetAddress
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class Zigbee2MqttAdapter(
    owningPluginId: String,
    private val mqttBroker: MqttBrokerService,
    eventsSink: EventsSink,
    repository: Repository,
) : HardwareAdapterBase<ZigbeePort<*>>(owningPluginId, "0", eventsSink), MqttListener {

    private var permitJoin: Boolean = false
    private var idBuilder = PortIdBuilder(owningPluginId)
    private val gson = Gson()
    private val debounceQueue = LinkedBlockingQueue<ZigbeeActionInputPort>()
    var operationScope: CoroutineScope? = null

    init {
        repository
            .getAllPorts()
            .filter { it.factoryId == owningPluginId && it.adapterId == id }
            .forEach {
                ports[it.id] = convertPortDtoToZigbeePort(it)
            }
    }

    private fun convertPortDtoToZigbeePort(portDto: PortDto): ZigbeePort<*> {
        val portIdSplit = portDto.id.split(':')
        val ieeeAddress = portIdSplit[1]
        val suffix = portIdSplit[2].split("-")[0]
        val readTopic = "zigbee2mqtt/$ieeeAddress"

        return when (portDto.valueClazz) {
            BatteryCharge::class.java.name -> {
                ZigbeeBatteryInputPort(portDto.id, readTopic, portDto.sleepInterval, portDto.lastSeenTimestamp)
            }
            BinaryInput::class.java.name -> {
                ZigbeeActionInputPort(portDto.id, readTopic, suffix, portDto.sleepInterval, portDto.lastSeenTimestamp)
            }
            Humidity::class.java.name -> {
                ZigbeeHumidityInputPort(portDto.id, readTopic, portDto.sleepInterval, portDto.lastSeenTimestamp)
            }
            Temperature::class.java.name -> {
                ZigbeeTemperatureInputPort(portDto.id, readTopic, "c", portDto.sleepInterval, portDto.lastSeenTimestamp)
            }
            else -> {
                throw Exception("Cannot recreate port from its snapshot!")
            }
        }
    }

    override suspend fun internalDiscovery(mode: DiscoveryMode): List<ZigbeePort<*>> = coroutineScope {
        if (mode == DiscoveryMode.Manual) {
            permitToJoinProcedure()

            if (permitJoin) {
                logDiscovery("Pair new devices NOW! Counting one minute down...")

                (0..3).forEach {
                    delay(10_000)
                    logDiscovery("${30 - it * 10}...")
                }

                mqttBroker.publish("zigbee2mqtt/bridge/request/permit_join", "false")
            } else {
                logDiscovery("It was impossible to permit other devices to join. Is Zigbee2Mqtt bridge running?")
            }
        } else {
            logDiscovery("Discovery on startup is disabled! Press \"Start discovery\" button to pair new devices")
        }

        listOf()
    }

    private suspend fun permitToJoinProcedure() = coroutineScope {
        val permitJoinJob = async {
            (1..3).forEach {
                logDiscovery("Requesting new devices to join, trial $it")

                mqttBroker.publish("zigbee2mqtt/bridge/request/permit_join", "true")

                delay(5000)
            }
        }

        while (!permitJoin && permitJoinJob.isActive) {
            //waiting for Zigbee2Mqtt bridge for allowing other devices to join
        }

        if (permitJoinJob.isActive) {
            permitJoinJob.cancel()
        }
    }

    override fun executePendingChanges() {
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
        operationScope?.cancel("Stop called")
    }

    override fun start() {
        mqttBroker.addMqttListener(this)

        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                val port = debounceQueue.poll()
                if (port != null) {
                    delay(1000)
                    port.value = BinaryInput(false)
                    broadcastPortUpdate(port)
                }
            }
        }
    }


    override fun onPublish(clientID: String, topicName: String, msgAsString: String) {
        if (topicName.startsWith("zigbee2mqtt")) {
            when (topicName) {
                "zigbee2mqtt/bridge/response/permit_join" -> handlePermitJoinMessage(msgAsString)
                "zigbee2mqtt/bridge/event" -> handleBridgeEvent(msgAsString)
            }

            val nowMillis = Calendar.getInstance().timeInMillis

            ports
                .values
                .filter {  it.readTopic == topicName }
                .forEach {
                    val updatePayload = gson.fromJson(msgAsString, UpdatePayload::class.java)
                    it.tryUpdate(updatePayload)
                    it.lastSeenTimestamp = nowMillis
                    broadcastPortUpdate(it)
                    debounceActionPort(it)
                }
        }
    }

    private fun debounceActionPort(it: ZigbeePort<*>) {
        if (it is ZigbeeActionInputPort && it.value.value) {
            debounceQueue.add(it)
        }
    }

    private fun handlePermitJoinMessage(message: String) {
        val status = gson.fromJson(message, PermitJoinStatus::class.java)
        permitJoin = status.data.value
    }

    private fun handleBridgeEvent(message: String) {
        if (message.contains("\"type\":\"device_interview\"")) {
            val interviewData = gson.fromJson(message, InterviewData::class.java)
            addNewPorts(interviewData.data)
        }
    }

    private fun addNewPorts(interview: InterviewDetails) {
        if (interview.supported) {
            val isBatteryPowered = interview.definition.exposes.any { it.name == "battery" }
            val sleepInterval = if (isBatteryPowered) 25 * 3600 * 1000L else 3600 * 1000L

            val now = Calendar.getInstance()
            val newPorts = interview
                .definition
                .exposes
                .filter { it.access == 1 }
                .map {
                    when (it.name) {
                        "battery" -> batteryPortDiscovered(interview.ieee_address, sleepInterval, now.timeInMillis)
                        "temperature" -> temperaturePortDiscovered(it, interview.ieee_address, sleepInterval, now.timeInMillis)
                        "humidity" -> humidityPortDiscovered(interview.ieee_address, sleepInterval, now.timeInMillis)
                        "action" -> binaryInputPortDiscovered(it, interview.ieee_address, sleepInterval, now.timeInMillis)
                        else -> listOf()
                    }
                }.flatten()

            addPotentialNewPorts(newPorts)
        }
    }

    private fun binaryInputPortDiscovered(
        exposition: Exposition,
        ieeeAddress: String,
        sleepInterval: Long,
        nowMillis: Long
    ): List<ZigbeeActionInputPort> {
        if (exposition.values == null) {
            return listOf()
        }

        return exposition.values.map {
            val portId = idBuilder.buildPortId(ieeeAddress, 0.toString(), it)
            ZigbeeActionInputPort(portId, "zigbee2mqtt/$ieeeAddress", it, sleepInterval, nowMillis)
        }
    }

    private fun humidityPortDiscovered(
        ieeeAddress: String,
        sleepInterval: Long,
        nowMillis: Long
    ): List<ZigbeeHumidityInputPort> {
        val portId = idBuilder.buildPortId(ieeeAddress, 0.toString(), "H")
        return listOf(ZigbeeHumidityInputPort(portId, "zigbee2mqtt/$ieeeAddress", sleepInterval, nowMillis))
    }

    private fun temperaturePortDiscovered(
        exposition: Exposition,
        ieeeAddress: String,
        sleepInterval: Long,
        nowMillis: Long
    ): List<ZigbeeTemperatureInputPort> {
        val portId = idBuilder.buildPortId(ieeeAddress, 0.toString(), "T")
        return listOf(ZigbeeTemperatureInputPort(portId, "zigbee2mqtt/$ieeeAddress", exposition.unit, sleepInterval, nowMillis))
    }

    private fun batteryPortDiscovered(
        ieeeAddress: String,
        sleepInterval: Long,
        nowMillis: Long
    ): List<ZigbeeBatteryInputPort> {
        val portId = idBuilder.buildPortId(ieeeAddress, 0.toString(), "B")
        return listOf(ZigbeeBatteryInputPort(portId, "zigbee2mqtt/$ieeeAddress", sleepInterval, nowMillis))
    }

    override fun onDisconnected(clientID: String) {
    }

    override suspend fun onConnected(address: InetAddress) = withContext(Dispatchers.IO) {
    }
}

/*

zigbee2mqtt/bridge/response/permit_join
{"data":{"value":true},"status":"ok"}

zigbee2mqtt/bridge/event
{"data":{"friendly_name":"0x00124b0025033adc","ieee_address":"0x00124b0025033adc"},"type":"device_leave"}
{"data":{"friendly_name":"0x00124b0025033adc","ieee_address":"0x00124b0025033adc"},"type":"device_joined"}
{"data":{"friendly_name":"0x00124b0025033adc","ieee_address":"0x00124b0025033adc","status":"started"},"type":"device_interview"}
{"data":{"friendly_name":"0x00124b0025033adc","ieee_address":"0x00124b0025033adc"},"type":"device_announce"}
{
   "data":{
      "definition":{
         "description":"Temperature and humidity sensor",
         "exposes":[
            {
               "access":1,
               "description":"Remaining battery in %",
               "name":"battery",
               "property":"battery",
               "type":"numeric",
               "unit":"%",
               "value_max":100,
               "value_min":0
            },
            {
               "access":1,
               "description":"Measured temperature value",
               "name":"temperature",
               "property":"temperature",
               "type":"numeric",
               "unit":"°C"
            },
            {
               "access":1,
               "description":"Measured relative humidity",
               "name":"humidity",
               "property":"humidity",
               "type":"numeric",
               "unit":"%"
            },
            {
               "access":1,
               "description":"Voltage of the battery in millivolts",
               "name":"voltage",
               "property":"voltage",
               "type":"numeric",
               "unit":"mV"
            },
            {
               "access":1,
               "description":"Link quality (signal strength)",
               "name":"linkquality",
               "property":"linkquality",
               "type":"numeric",
               "unit":"lqi",
               "value_max":255,
               "value_min":0
            }
         ],
         "model":"SNZB-02",
         "options":[
            {
               "access":2,
               "description":"Number of digits after decimal point for temperature, takes into effect on next report of device.",
               "name":"temperature_precision",
               "property":"temperature_precision",
               "type":"numeric",
               "value_max":3,
               "value_min":0
            },
            {
               "access":2,
               "description":"Calibrates the temperature value (absolute offset), takes into effect on next report of device.",
               "name":"temperature_calibration",
               "property":"temperature_calibration",
               "type":"numeric"
            },
            {
               "access":2,
               "description":"Number of digits after decimal point for humidity, takes into effect on next report of device.",
               "name":"humidity_precision",
               "property":"humidity_precision",
               "type":"numeric",
               "value_max":3,
               "value_min":0
            },
            {
               "access":2,
               "description":"Calibrates the humidity value (absolute offset), takes into effect on next report of device.",
               "name":"humidity_calibration",
               "property":"humidity_calibration",
               "type":"numeric"
            }
         ],
         "supports_ota":false,
         "vendor":"SONOFF"
      },
      "friendly_name":"0x00124b0025033adc",
      "ieee_address":"0x00124b0025033adc",
      "status":"successful",
      "supported":true
   },
   "type":"device_interview"
}



{
   "data":{
      "definition":{
         "description":"Wireless button",
         "exposes":[
            {
               "access":1,
               "description":"Remaining battery in %",
               "name":"battery",
               "property":"battery",
               "type":"numeric",
               "unit":"%",
               "value_max":100,
               "value_min":0
            },
            {
               "access":1,
               "description":"Triggered action (e.g. a button click)",
               "name":"action",
               "property":"action",
               "type":"enum",
               "values":[
                  "single",
                  "double",
                  "long"
               ]
            },
            {
               "access":1,
               "description":"Voltage of the battery in millivolts",
               "name":"voltage",
               "property":"voltage",
               "type":"numeric",
               "unit":"mV"
            },
            {
               "access":1,
               "description":"Link quality (signal strength)",
               "name":"linkquality",
               "property":"linkquality",
               "type":"numeric",
               "unit":"lqi",
               "value_max":255,
               "value_min":0
            }
         ],
         "model":"SNZB-01",
         "options":[

         ],
         "supports_ota":false,
         "vendor":"SONOFF"
      },
      "friendly_name":"0x00124b0025047354",
      "ieee_address":"0x00124b0025047354",
      "status":"successful",
      "supported":true
   },
   "type":"device_interview"
}

-> zigbee2mqtt/bridge/config/devices/get
-< zigbee2mqtt/bridge/config/devices
[
   {
      "dateCode":"20210708",
      "friendly_name":"Coordinator",
      "ieeeAddr":"0x00124b0024c31ef0",
      "lastSeen":1648976981847,
      "networkAddress":0,
      "softwareBuildID":"zStack3x0",
      "type":"Coordinator"
   },
   {
      "dateCode":"20211103",
      "description":"Wireless button",
      "friendly_name":"0x00124b0025047354",
      "hardwareVersion":0,
      "ieeeAddr":"0x00124b0025047354",
      "lastSeen":1648976030756,
      "manufacturerID":0,
      "manufacturerName":"eWeLink",
      "model":"SNZB-01",
      "modelID":"WB01",
      "networkAddress":63941,
      "powerSource":"Battery",
      "type":"EndDevice",
      "vendor":"SONOFF"
   },
   {
      "dateCode":"20211103",
      "description":"Temperature and humidity sensor",
      "friendly_name":"0x00124b0025033adc",
      "hardwareVersion":1,
      "ieeeAddr":"0x00124b0025033adc",
      "lastSeen":1648976870850,
      "manufacturerID":0,
      "manufacturerName":"eWeLink",
      "model":"SNZB-02",
      "modelID":"TH01",
      "networkAddress":62361,
      "powerSource":"Battery",
      "type":"EndDevice",
      "vendor":"SONOFF"
   }
]
 */