package eu.automateeverything.shellyplugin

import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PortUpdateEventData
import eu.automateeverything.domain.hardware.*
import eu.automateeverything.domain.mqtt.MqttBrokerService
import eu.automateeverything.domain.mqtt.MqttListener
import eu.automateeverything.domain.langateway.LanGateway
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.shellyplugin.ports.ShellyInputPort
import eu.automateeverything.shellyplugin.ports.ShellyOutputPort
import eu.automateeverything.shellyplugin.ports.ShellyPort
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.net.Inet4Address
import java.net.InetAddress
import java.util.*

class ShellyAdapter(private val owningPluginId: String,
                    private val mqttBroker: MqttBrokerService,
                    lanGatewayResolver: LanGatewayResolver) : HardwareAdapterBase<ShellyPort<*>>(), MqttListener {
    override val id = ADAPTER_ID
    private var brokerIP: Inet4Address? = null
    private var idBuilder = PortIdBuilder(owningPluginId)
    private var updateSink: EventsSink? = null
    private val client = createHttpClient()
    private val lanGateways: List<LanGateway> = lanGatewayResolver.resolve()

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

    override suspend fun internalDiscovery(eventsSink: EventsSink): ArrayList<ShellyPort<*>> = coroutineScope {
        val result = ArrayList<ShellyPort<*>>()

        if (lanGateways.isEmpty()) {
            eventsSink.broadcastDiscoveryEvent(
                owningPluginId,
                "The IP address of MQTT broker cannot be resolved - no LAN gateways! Aborting"
            )
        } else {
            val defaultLanGateway = lanGateways.first()
            if (lanGateways.size > 1) {
                eventsSink.broadcastDiscoveryEvent(
                    owningPluginId,
                    "WARNING! There's more than one LAN gateway. It's impossible to determine the correct IP address of MQTT broker (which should be same as Lan gateway). Using ${defaultLanGateway.interfaceName}"
                )
            }
            brokerIP = defaultLanGateway.inet4Address
            val discoveryJob = async { ShellyHelper.searchForShellies(owningPluginId, client, brokerIP!!, eventsSink) }
            val shellies = discoveryJob.await()

            shellies.forEach {
                val shellyIP = it.first
                val settingsResponse = it.second
                val shellyId = settingsResponse.device.hostname
                ShellyHelper.hijackShellyIfNeeded(client, brokerIP!!, settingsResponse, shellyIP)
                val statusResponse = ShellyHelper.callForStatus(client, shellyIP)

                val portsFromDevice = ShellyPortFactory().constructPorts(shellyId, idBuilder, statusResponse, settingsResponse)
                result.addAll(portsFromDevice)
            }
        }

        result
    }

    override fun executePendingChanges() {
        ports
            .values
            .filterIsInstance<ShellyOutputPort<*>>()
            .forEach {
                executeShellyChanges(it)
            }
    }

    private fun executeShellyChanges(shellyOutput: ShellyOutputPort<*>) {
        val mqttPayload = shellyOutput.getExecutePayload()
        if (mqttPayload != null) {
            val topic = shellyOutput.writeTopic
            mqttBroker.publish(topic, mqttPayload)
        }
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.updateSink = operationSink
        mqttBroker.addMqttListener(this)
    }

    override fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(clientID: String, topicName: String, msgAsString: String) {
        val now = Calendar.getInstance().timeInMillis

        ports
            .values
            .filter { it.id.contains(clientID) }
            .forEach { it.updateValidUntil(now + it.sleepInterval) }

        ports
            .values
            .filter { (it as ShellyInputPort<*>?)?.readTopic == topicName }
            .forEach {
                (it as ShellyInputPort<*>?)?.setValueFromMqttPayload(msgAsString)

                val updateEvent = PortUpdateEventData(owningPluginId, id, it)
                updateSink?.broadcastEvent(updateEvent)
            }
    }

    override fun onDisconnected(clientID: String) {
        ports
            .values
            .forEach {
                val port = it
                if (port.id.contains(clientID)) {
                    port.markDisconnected()
                }
            }
    }

    override suspend fun onConnected(address: InetAddress) = withContext(Dispatchers.IO) {
        val finderResponse = ShellyHelper.checkIfShelly(owningPluginId, client, address, null)
        if (finderResponse != null) {
            val statusResponse = ShellyHelper.callForStatus(client, address)
            val settingsResponse = ShellyHelper.callForSettings(client, address)
            val shellyId = finderResponse.second.device.hostname
            val portsFromDevice = ShellyPortFactory().constructPorts(
                shellyId,
                idBuilder,
                statusResponse,
                settingsResponse
            )

            addPotentialNewPorts(portsFromDevice)
        }
    }

    companion object {
        const val ADAPTER_ID = "0"
    }
}