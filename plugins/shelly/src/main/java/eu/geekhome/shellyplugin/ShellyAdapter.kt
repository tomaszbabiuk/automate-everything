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

    private var finder: ShellyFinder
    private val client = createHttpClient()
    private val brokerIP: InetAddress?
    private val gson: Gson
    private val operators = ArrayList<ShellyInPortOperator<*>>()

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
            val addresses = LanInetAddressHelper.getIpsInLan();
            return addresses[addresses.size - 1]
        } catch (ex: Exception) {
            logger.error("Problem resolving this machine IP in LAN", ex)
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
        client.get<String>("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server=" + brokerIP!!.hostAddress + "%3A1883")
    }

    @Throws(IOException::class)
    private suspend fun disableCloud(shellyIP: InetAddress) {
        client.get<String>("http://$shellyIP/settings/cloud?enabled=0")
    }

    @Throws(IOException::class)
    private suspend fun callForLightResponse(possibleShellyIP: InetAddress, channel: Int): LightResponseDto {
        return client.get("http://$possibleShellyIP/light/$channel")
    }

    @Throws(IOException::class)
    private suspend fun callForRelayResponse(possibleShellyIP: InetAddress, channel: Int): RelayResponseDto {
        return client.get("http://$possibleShellyIP/relay/$channel")
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
            val shellyId = settingsResponse.device.hostname
            hijackShellyIfNeeded(settingsResponse, shellyIP)

            //TODO: check if that's battery powered device to calculate connection interval
//            val isBatteryPowered = false
//            val connectionLostInterval = if (isBatteryPowered) 60 * 60 * 1000L else 40 * 60 * 1000L
            if (settingsResponse.relays != null) {
                for (i in settingsResponse.relays.indices) {
                    val relayResponse = callForRelayResponse(shellyIP, i)
                    val (relayPort, relayOperator) = constructRelayInOutPort(idBuilder, shellyId, i, relayResponse)
                    operators.add(relayOperator)
                    result.add(relayPort)
                }

                if (settingsResponse.device.num_meters > 0) {
                    for (i in 0 until settingsResponse.device.num_meters) {
                        val (meterPort, meterOperator) = constructRelayWattageInPort(idBuilder, shellyId, i)
                        result.add(meterPort)
                        operators.add(meterOperator)
                    }
                }
            }

            if (settingsResponse.lights != null) {
                for (i in settingsResponse.lights.indices) {
                    val lightResponse = callForLightResponse(shellyIP, i)
                    val (lightPort, lightOperator) = constructPowerLevelInOutPort(idBuilder, shellyId, i, lightResponse)
                    result.add(lightPort)
                    operators.add(lightOperator)

                    if (settingsResponse.device.num_meters > 0) {
                        val (meterPort, meterOperator) = constructLightWattageInPort(idBuilder, shellyId, i)
                        result.add(meterPort)
                        operators.add(meterOperator)
                    }
                }
            }
        }

        result
    }

    private fun constructRelayInOutPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto
    ): Pair<RelayInOutPort, ShellyRelayInOutPortOperator> {
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val operator = ShellyRelayInOutPortOperator(shellyId, channel)
        operator.setValueFromRelayResponse(relayResponse)
        val port = RelayInOutPort(id, operator, operator)

        return Pair(port, operator)
    }

    private fun constructRelayWattageInPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ): Pair<WattageInPort, ShellyRelayWattageInPortOperator> {
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val operator = ShellyRelayWattageInPortOperator(shellyId, channel)
        val port = WattageInPort(id, operator)

        return Pair(port, operator)
    }

    private fun constructPowerLevelInOutPort(
        idBuilder: PortIdBuilder,
        shellyId : String,
        channel: Int,
        lightResponse: LightResponseDto
    ): Pair<PowerLevelInOutPort, PowerLevelInOutPortOperator> {
        val operator = PowerLevelInOutPortOperator(shellyId, channel)
        operator.setValueFromLightResponse(lightResponse)
        val id = idBuilder.buildPortId(shellyId, channel, "L")
        val port = PowerLevelInOutPort(id, operator, operator)

        return Pair(port, operator)
    }

    private fun constructLightWattageInPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int
    ) : Pair<WattageInPort, LightWattageInPortOperator> {
        val operator = LightWattageInPortOperator(shellyId, channel)
        val id = idBuilder.buildPortId(shellyId, channel, "W")
        val port = WattageInPort(id, operator)

        return Pair(port, operator)
    }

    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
    }

    override fun resetLatches() {
        operators
            .map { it as? ShellyOutPortOperator<*> }
            .filterNotNull()
            .forEach {
                executeShellyChanges(it)
            }
    }

    private fun executeShellyChanges(shellyOutput: ShellyOutPortOperator<*>) {
        if (shellyOutput.isLatchTriggered()) {
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
        val now = Calendar.getInstance().timeInMillis

        operators
            .filter { it.readTopic == topicName }
            .forEach {
                it.setValueFromMqttPayload(payload)
                //it.updateLastSeenTimestamp(now)
            }
    }

    override fun onDisconnected(clientID: String) {
//        iterateAllOwnedPorts(object : PortIterateListener {
//            override fun onIteratePort(port: IShellyPort) {
//                if (port.id.startsWith(clientID)) {
//                    port.markDisconnected()
//                }
//            }
//        })
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