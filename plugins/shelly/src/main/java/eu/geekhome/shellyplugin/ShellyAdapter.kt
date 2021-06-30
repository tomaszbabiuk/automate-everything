package eu.geekhome.shellyplugin

import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.LiveEventsHelper
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.*
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.mqtt.MqttListener
import eu.geekhome.domain.repository.SettingsDto
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.langateway.LanGateway
import eu.geekhome.shellyplugin.operators.ShellyReadPortOperator
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.net.Inet4Address
import java.net.InetAddress
import java.util.*

class ShellyAdapter(owningPluginId: String, private val mqttBroker: MqttBrokerService) : HardwareAdapterBase(), MqttListener {

    private var brokerIP: Inet4Address? = null
    private var idBuilder = PortIdBuilder(owningPluginId, id)
    private var updateSink: EventsSink? = null
    private val client = createHttpClient()
    private var hasNewPorts = false
    private val lanGateways: List<LanGateway> = JavaLanGatewayResolver().resolve()
    override val ports = ArrayList<ShellyPort<*>>()

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

    @ExperimentalCoroutinesApi
    override suspend fun internalDiscovery(eventsSink: EventsSink) = coroutineScope {
        ports.clear()

        if (lanGateways.isEmpty()) {
            LiveEventsHelper.broadcastEvent(
                eventsSink,
                ShellyPlugin.PLUGIN_ID_SHELLY,
                "The IP address of MQTT broker cannot be resolved - no LAN gateways! Aborting"
            )
        } else {
            val defaultLanGateway = lanGateways.first()
            if (lanGateways.size > 1) {
                LiveEventsHelper.broadcastEvent(
                    eventsSink,
                    ShellyPlugin.PLUGIN_ID_SHELLY,
                    "WARNING! There's more than one LAN gateway. It's impossible to determine the correct IP address of MQTT broker (which should be same as Lan gateway). Using ${defaultLanGateway.interfaceName}"
                )
            }
            brokerIP = defaultLanGateway.inet4Address
            val discoveryJob = async { ShellyHelper.searchForShellies(client, brokerIP!!, eventsSink) }
            val shellies = discoveryJob.await()

            shellies.forEach {
                val shellyIP = it.first
                val settingsResponse = it.second
                val shellyId = settingsResponse.device.hostname
                ShellyHelper.hijackShellyIfNeeded(client, brokerIP!!, settingsResponse, shellyIP)
                val statusResponse = ShellyHelper.callForStatus(client, shellyIP)

                val portsFromDevice = ShellyPortFactory().constructPorts(shellyId, idBuilder, statusResponse, settingsResponse)
                ports.addAll(portsFromDevice)
            }
        }
    }

    override fun clearNewPorts() {
        hasNewPorts = false
    }

    override fun hasNewPorts(): Boolean {
        return false
    }

    override fun executePendingChanges() {
        ports
            .mapNotNull { it.writePortOperator }
            .filterIsInstance<ShellyWritePortOperator<*>>()
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
            .filter { it.id.contains(clientID) }
            .forEach { it.updateValidUntil(now + it.sleepInterval) }

        ports
            .filter { (it.readPortOperator as ShellyReadPortOperator<*>?)?.readTopic == topicName }
            .forEach {
                (it.readPortOperator as ShellyReadPortOperator<*>?)?.setValueFromMqttPayload(msgAsString)

                val updateEvent = PortUpdateEventData(ShellyPlugin.PLUGIN_ID_SHELLY, id, it)
                updateSink?.broadcastEvent(updateEvent)
            }
    }

    override fun onDisconnected(clientID: String) {
        ports
            .forEach {
                val port = it
                if (port.id.contains(clientID)) {
                    port.markDisconnected()
                }
            }
    }

    override suspend fun onConnected(address: InetAddress) = withContext(Dispatchers.IO) {
        val finderResponse = ShellyHelper.checkIfShelly(client, address, null)
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

            portsFromDevice.forEach { newPort ->
                val isAlreadyDiscovered = ports.find { it.id == newPort.id} != null
                if (!isAlreadyDiscovered) {
                    hasNewPorts = true
                }
            }
        }
    }
}