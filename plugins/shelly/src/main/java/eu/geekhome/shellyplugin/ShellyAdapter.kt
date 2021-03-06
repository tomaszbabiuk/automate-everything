package eu.geekhome.shellyplugin

import com.google.gson.Gson
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.*
import eu.geekhome.services.mqtt.MqttBrokerService
import eu.geekhome.services.mqtt.MqttListener
import eu.geekhome.shellyplugin.operators.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class ShellyAdapter(factoryId: String, private val mqttBroker: MqttBrokerService) : HardwareAdapterBase(), MqttListener {

    override val newPorts = ArrayList<ConnectiblePort<*>>()
    private var idBuilder = PortIdBuilder(factoryId, id)
    private var updateSink: EventsSink<PortUpdateEvent>? = null
    private var finder: ShellyFinder
    private val client = createHttpClient()
    private val brokerIP: InetAddress?
    private val gson: Gson
    private val ports = ArrayList<ConnectiblePort<*>>()

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
        return client.get("http://$shellyIP/status")
    }

    @Throws(IOException::class)
    private suspend fun disableCloud(shellyIP: InetAddress) {
        client.get<String>("http://$shellyIP/settings/cloud?enabled=0")
    }

    @ExperimentalCoroutinesApi
    override suspend fun internalDiscovery(
        eventsSink: EventsSink<HardwareEvent>
    ): MutableList<Port<*>> = coroutineScope {
        ports.clear()

        val discoveryJob = async { finder.searchForShellies(eventsSink) }
        val shellies = discoveryJob.await()
        shellies.forEach {
            val shellyIP = it.first
            val settingsResponse = it.second
            val shellyId = settingsResponse.device.hostname
            hijackShellyIfNeeded(settingsResponse, shellyIP)
            val statusResponse = callForStatus(shellyIP)

            //TODO: check if that's battery powered device to calculate connection interval
//            val isBatteryPowered = false
//            val connectionLostInterval = if (isBatteryPowered) 60 * 60 * 1000L else 40 * 60 * 1000L
            val portsFromDevice = ShellyPortFactory().constructPorts(shellyId, idBuilder, statusResponse)
            ports.addAll(portsFromDevice)
        }

        ports.toMutableList()
    }

    override fun clearNewPorts() {
        newPorts.clear()
    }

    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
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

        ports
            .filter { (it.readPortOperator as ShellyReadPortOperator<*>?)?.readTopic == topicName }
            .forEach {
                (it.writePortOperator as ShellyReadPortOperator<*>?)?.setValueFromMqttPayload(payload)
                it.updateLastSeen(now)

                val updateEvent = PortUpdateEvent(ShellyPlugin.PLUGIN_ID_SHELLY, id, it)
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

    override fun onConnected(address: InetAddress) {
        GlobalScope.launch {
            val finderResponse = finder.checkIfShelly(address, null)
            if (finderResponse != null) {
                val statusResponse = callForStatus(address)
                val shellyId = finderResponse.second.device.hostname
                val portsFromDevice = ShellyPortFactory().constructPorts(shellyId, idBuilder, statusResponse)
                portsFromDevice.forEach { newPort ->
                    val isAlreadyDiscovered = ports.find { it.id == newPort.id} != null
                    if (!isAlreadyDiscovered) {
                        val foundAsNewAlready = newPorts.find { it.id == newPort.id} != null
                        if (!foundAsNewAlready) {
                            newPorts.add(newPort)
                        }
                    }
                }
            }
        }
    }

    init {
        brokerIP = resolveIpInLan()
        finder = ShellyFinder(client, brokerIP!!)
        gson = Gson()
    }
}