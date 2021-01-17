package eu.geekhome.shellyplugin

import com.geekhome.common.OperationMode
import com.geekhome.common.logging.LoggingService
import com.google.gson.Gson
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.*
import eu.geekhome.services.mqtt.MqttBrokerService
import eu.geekhome.services.mqtt.MqttListener
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class ShellyAdapter(private val mqttBroker: MqttBrokerService) : HardwareAdapterBase(), MqttListener {
    private interface PortIterateListener {
        fun onIteratePort(port: IShellyPort)
    }

    private var finder: ShellyFinder
    private val client = createHttpClient()
    private val brokerIP: InetAddress?
    private val gson: Gson
    private val ownedDigitalOutputPorts = ArrayList<ShellyDigitalOutputPort>()
    private val ownedPowerInputPorts = ArrayList<ShellyPowerInputPort>()
    private val ownedPowerOutputPorts = ArrayList<ShellyPowerOutputPort>()

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature)
        engine {
            maxConnectionsCount = 1000

            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
    }


    private fun resolveIpInLan(): InetAddress? {
        try {
            val addresses = LanInetAddressHelper.getIpsInLan();
            return addresses[addresses.size - 1]
        } catch (ex: Exception) {
            logger.error("Problem resolving this machine IP in LAN", ex)
        }
        return null
    }

    @Throws(IOException::class)
    private suspend fun hijackShellyIfNeeded(settings: ShellySettingsResponse, shellyIP: InetAddress) {
        if (settings.cloud.isEnabled) {
            disableCloud(shellyIP)
        }
        val expectedMqttServerSetting = brokerIP!!.hostAddress + ":1883"
        if (!settings.mqtt.isEnable || settings.mqtt.server != expectedMqttServerSetting) {
            enableMQTT(shellyIP)
        }
    }


    @Throws(IOException::class)
    private suspend fun enableMQTT(shellyIP: InetAddress) {
        val response = client.get<String>("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server=" + brokerIP!!.hostAddress + "%3A1883")
//        val request = Request.Builder()
//            .url("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server=" + brokerIP!!.hostAddress + "%3A1883")
//            .build()
//        val response = okClient.newCall(request).execute()
//        if (!response.isSuccessful) {
//            throw IOException("Unexpected code $response")
//        }
    }

    @Throws(IOException::class)
    private suspend fun disableCloud(shellyIP: InetAddress) {
        val response = client.get<String>("http://$shellyIP/settings/cloud?enabled=0")
//        val request = Request.Builder()
//            .url("http://$shellyIP/settings/cloud?enabled=0")
//            .build()
//        val response = okClient.newCall(request).execute()
//        if (!response.isSuccessful) {
//            throw IOException("Unexpected code $response")
//        }
    }

    @Throws(IOException::class)
    private suspend fun callForLightResponse(possibleShellyIP: InetAddress, channel: Int): ShellyLightResponse {
        return client.get("http://$possibleShellyIP/light/$channel")

//        val request = Request.Builder()
//            .url("http://$possibleShellyIP/light/$channel")
//            .build()
//        val response = okClient.newCall(request).execute()
//        val json = response.body!!.string()
//        response.body!!.close()
//        return gson.fromJson(json, ShellyLightResponse::class.java)
    }



    @ExperimentalCoroutinesApi
    override suspend fun internalDisvovery(
        idBuilder: PortIdBuilder,
        eventsSink: EventsSink<String>
    ): MutableList<Port<*, *>> = coroutineScope {
        val result = ArrayList<Port<*, *>>()

        val discoveryJob = async { finder.searchForShellies(eventsSink) }
        val shellies = discoveryJob.await()
        shellies.forEach {
            val shellyIP = it.first
            val settingsResponse = it.second
            hijackShellyIfNeeded(settingsResponse, shellyIP)

            //TODO: check if that's battery powered device to calculate connection interval
//            val isBatteryPowered = false
//            val connectionLostInterval = if (isBatteryPowered) 60 * 60 * 1000L else 40 * 60 * 1000L
//            if (settingsResponse.relays != null) {
//                for (i in settingsResponse.relays.indices) {
//                    val output =
//                        ShellyDigitalOutputPort(settingsResponse, i, connectionLostInterval)
//                    digitalOutputPorts.add(output)
//                    ownedDigitalOutputPorts.add(output)
//                }
//            }
//            if (settingsResponse.lights != null) {
//                for (i in settingsResponse.lights.indices) {
//                    val lightResponse = callForLightResponse(shellyIP, i)
//                    val output = ShellyPowerOutputPort(
//                        settingsResponse,
//                        lightResponse,
//                        i,
//                        connectionLostInterval
//                    )
//                    powerOutputPorts.add(output)
//                    ownedPowerOutputPorts.add(output)
//                }
//            }
            if (settingsResponse.meters != null) {
                for (i in 0 until settingsResponse.device.numMeters) {
                    val id = idBuilder.buildPortId(settingsResponse.device.hostname, i)
                    val inputPort = WattageInPort(id, 0.0, SynchronizedReadOperator(Wattage(0.0)))
                    result.add(inputPort)
                }
            }
        }

        result
    }





    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
    }

    override fun resetLatches() {
        for (port in ownedDigitalOutputPorts) {
            resetShellyLatch(port)
        }
        for (port in ownedPowerOutputPorts) {
            resetShellyLatch(port)
        }
    }

    private fun resetShellyLatch(shellyOutput: IShellyOutputPort) {
        if (shellyOutput.didChangeValue()) {
            val mqttPayload = shellyOutput.convertValueToMqttPayload()
            val topic = shellyOutput.writeTopic
            mqttBroker.publish(topic, mqttPayload)
            shellyOutput.resetLatch()
        }
    }

    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

    override fun start() {
        mqttBroker.addMqttListener(this)
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(topicName: String, payload: String) {
        logger.info("MQTT message: $topicName, content: $payload")
        val isRelayTopic = topicName.contains("/relay/")
        val isLightTopic = topicName.contains("/light/")
        val now = Calendar.getInstance().timeInMillis
        if (!isRelayTopic && !isLightTopic) {
            return
        }
        iterateAllOwnedPorts(object : PortIterateListener {
            override fun onIteratePort(port: IShellyPort) {
                if (topicName == port.readTopic) {
                    port.setValueFromMqttPayload(payload)
                    port.updateLastSeenTimestamp(now)
                }
            }
        })
    }

    override fun onDisconnected(clientID: String) {
        iterateAllOwnedPorts(object : PortIterateListener {
            override fun onIteratePort(port: IShellyPort) {
                if (port.id.startsWith(clientID)) {
                    port.markDisconnected()
                }
            }
        })
    }

    private fun iterateAllOwnedPorts(listener: PortIterateListener) {
        for (port in ownedDigitalOutputPorts) {
            listener.onIteratePort(port)
        }
        for (port in ownedPowerOutputPorts) {
            listener.onIteratePort(port)
        }
        for (port in ownedPowerInputPorts) {
            listener.onIteratePort(port)
        }
    }

    companion object {
        private val logger = LoggingService.getLogger()
    }

    init {
        brokerIP = resolveIpInLan()
        finder = ShellyFinder(client, brokerIP!!)
        gson = Gson()
    }
}