package eu.geekhome.shellyplugin

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

typealias Triplet = Triple<ConnectiblePort<*>, ShellyReadPortOperator<*>?, ShellyWritePortOperator<*>?>

class ShellyAdapter(private val mqttBroker: MqttBrokerService) : HardwareAdapterBase(), MqttListener {

    private var updateSink: EventsSink<PortUpdateEvent>? = null
    private var finder: ShellyFinder
    private val client = createHttpClient()
    private val brokerIP: InetAddress?
    private val gson: Gson
    private val triplets = ArrayList<Triplet>()

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }

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
            val addresses = LanInetAddressHelper.ipsInLan
            return addresses[addresses.size - 2]
        } catch (ex: Exception) {
            //TODO: fix LAN IP finder
        }
        return null
    }

    @Throws(IOException::class)
    private suspend fun hijackShellyIfNeeded(settings: ShellySettingsResponse, shellyIP: InetAddress) {
        if (settings.cloud.enabled) {
            disableCloud(shellyIP)
        }
        val expectedMqttServerSetting = brokerIP!!.hostAddress + ":1883"
        if (!settings.mqtt.enable || settings.mqtt.server != expectedMqttServerSetting) {
            enableMQTT(shellyIP)
        }
    }


    @Throws(IOException::class)
    private suspend fun enableMQTT(shellyIP: InetAddress) {
        client.get<String>("""http://$shellyIP/settings/mqtt?mqtt_enable=1&mqtt_server=${brokerIP!!.hostAddress}%3A1883""")
    }

    @Throws(IOException::class)
    private suspend fun callForStatus(shellyIP: InetAddress): ShellyStatusResponse {
        return client.get<ShellyStatusResponse>("http://$shellyIP/status")
    }

    @Throws(IOException::class)
    private suspend fun disableCloud(shellyIP: InetAddress) {
        client.get<String>("http://$shellyIP/settings/cloud?enabled=0")
    }

    @Throws(IOException::class)
    private suspend fun callForRelayResponse(possibleShellyIP: InetAddress, channel: Int): RelayResponseDto {
        return client.get("http://$possibleShellyIP/relay/$channel")
    }

    @ExperimentalCoroutinesApi
    override suspend fun internalDiscovery(
        idBuilder: PortIdBuilder,
        eventsSink: EventsSink<HardwareEvent>
    ): MutableList<Port<*>> = coroutineScope {
        triplets.clear()

        val discoveryJob = async { finder.searchForShellies(eventsSink) }
        val shellies = discoveryJob.await()
        shellies.forEach {
            val shellyIP = it.first
            val settingsResponse = it.second
            val shellyId = settingsResponse.device.hostname
            hijackShellyIfNeeded(settingsResponse, shellyIP)
            val statusResponse = callForStatus(shellyIP)

            //TODO: check if that's battery powered device to calculate connection interval
            val isBatteryPowered = false
            val connectionLostInterval = if (isBatteryPowered) 60 * 60 * 1000L else 40 * 60 * 1000L
            if (statusResponse.relays != null) {
                for (i in statusResponse.relays.indices) {
                    val relayResponse = callForRelayResponse(shellyIP, i)
                    triplets.add(constructRelayInOutPort(idBuilder, shellyId, i, relayResponse))
                }

                if (statusResponse.meters != null) {
                    for (i in statusResponse.meters.indices) {
                        triplets.add(constructRelayWattageReadPort(idBuilder, shellyId, i))
                    }
                }
            }

            if (statusResponse.lights != null) {
                for (i in statusResponse.lights.indices) {
                    val lightResponse = statusResponse.lights[i]
                    triplets.add(constructPowerLevelReadWritePort(idBuilder, shellyId, i, lightResponse))
                }

                if (statusResponse.meters != null) {
                    for (i in statusResponse.meters.indices) {
                        triplets.add(constructLightWattageReadPort(idBuilder, shellyId, i))
                    }
                }
            }

            if (statusResponse.tmp != null) {
                triplets.add(constructTemperatureReadPort(idBuilder, shellyId, statusResponse.tmp))
            }

            if (statusResponse.hum != null) {
                triplets.add(constructHumidityReadPort(idBuilder, shellyId, statusResponse.hum))
            }

            if (statusResponse.bat != null) {
                triplets.add(constructBatteryReadPort(idBuilder, shellyId, statusResponse.bat))
            }
        }

        triplets
            .map {it.first}
            .toMutableList()
    }

    private fun constructBatteryReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        batteryBrief: BatteryBriefDto
    ): Triplet {
        val id = idBuilder.buildPortId(shellyId, 0, "B")
        val operator = ShellyBatteryReadPortOperator(shellyId)
        operator.setValueFromBatteryResponse(batteryBrief)
        val port = ConnectiblePort(id, BatteryCharge::class.java,
            readPortOperator = operator,
        )

        return Triple(port, operator, null)
    }

    private fun constructHumidityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        humidityBrief: HumidityBriefDto
    ): Triplet {
        val id = idBuilder.buildPortId(shellyId, 0, "H")
        val operator = ShellyHumidityReadPortOperator(shellyId)
        operator.setValueFromHumidityResponse(humidityBrief)
        val port = ConnectiblePort(id, Humidity::class.java,
            readPortOperator = operator,
        )

        return Triple(port, operator, null)
    }

    private fun constructTemperatureReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        temperatureBrief: TemperatureBriefDto
    ): Triplet {
        val id = idBuilder.buildPortId(shellyId, 0, "T")
        val operator = ShellyTemperatureReadPortOperator(shellyId)
        operator.setValueFromTemperatureResponse(temperatureBrief)
        val port = ConnectiblePort(id, Temperature::class.java,
            readPortOperator = operator,
        )

        return Triple(port, operator, null)
    }

    private fun constructRelayInOutPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto
    ): Triplet {
        val id = idBuilder.buildPortId(shellyId, channel, "R")
        val operator = ShellyRelayReadWritePortOperator(shellyId, channel)
        operator.setValueFromRelayResponse(relayResponse)
        val port = ConnectiblePort(id, Relay::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )

        return Triple(port, operator, operator)
    }

    private fun constructRelayWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ): Triplet {
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val operator = ShellyRelayWattageReadPortOperator(shellyId, channel)
        val port = ConnectiblePort(id, Wattage::class.java,
            readPortOperator = operator
        )

        return Triplet(port, operator, null)
    }

    private fun constructPowerLevelReadWritePort(
        idBuilder: PortIdBuilder,
        shellyId : String,
        channel: Int,
        lightResponse: LightBriefDto
    ): Triplet {
        val operator = PowerLevelReadWritePortOperator(shellyId, channel)
        operator.setValueFromLightResponse(lightResponse)
        val id = idBuilder.buildPortId(shellyId, channel, "L")
        val port = ConnectiblePort(id, PowerLevel::class.java,
            readPortOperator = operator,
            writePortOperator = operator
        )
        return Triplet(port, operator, operator)
    }

    private fun constructLightWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ) : Triplet {
        val operator = LightWattageReadPortOperator(shellyId, channel)
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val port = ConnectiblePort(id, Wattage::class.java,
            readPortOperator = operator
        )

        return Triplet(port, operator, null)
    }

    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
    }

    override fun executePendingChanges() {
        triplets
            .mapNotNull { it.third }
            .forEach {
                executeShellyChanges(it)
            }
    }

    private fun executeShellyChanges(shellyOutput: ShellyWritePortOperator<*>) {
        val mqttPayload = shellyOutput.getExecutePayload()
        if (mqttPayload != null) {
            val topic = shellyOutput.writeTopic
            mqttBroker.publish(topic, mqttPayload)
            shellyOutput.reset()
        }
    }

    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

    override fun start(updateSink: EventsSink<PortUpdateEvent>) {
        this.updateSink = updateSink
        mqttBroker.addMqttListener(this)
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(topicName: String, payload: String) {
        val now = Calendar.getInstance().timeInMillis

        triplets
            .filter { it.second?.readTopic == topicName }
            .forEach {
                it.second?.setValueFromMqttPayload(payload)
                it.first.updateLastSeen(now)

                val updateEvent = PortUpdateEvent(ShellyPlugin.PLUGIN_ID_SHELLY, id, it.first)
                updateSink?.broadcastEvent(updateEvent)
            }
    }

    override fun onDisconnected(clientID: String) {
        triplets
            .forEach {
                val port = it.first
                if (port.id.contains(clientID)) {
                    port.markDisconnected()
                }
            }
    }

    init {
        brokerIP = resolveIpInLan()
        finder = ShellyFinder(client, brokerIP!!)
        gson = Gson()
    }
}