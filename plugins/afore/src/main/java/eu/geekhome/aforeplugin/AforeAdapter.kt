package eu.geekhome.aforeplugin

import eu.geekhome.domain.events.DiscoveryEventData
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.OperationMode
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.PortIdBuilder
import eu.geekhome.domain.repository.SettingsDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import java.util.*

class AforeAdapter(private val owningPluginId: String) : HardwareAdapterBase() {

    private var operationSink: EventsSink? = null
    override val newPorts = ArrayList<Port<*>>()
    private val httpClient = createHttpClient()
    private val idBuilder = PortIdBuilder(owningPluginId, ADAPTER_ID)
    private var ports = ArrayList<AforeWattagePort>()

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        install(Auth) {
            basic {
                password = "admin"
                username = "admin"
            }
        }

        engine {
            maxConnectionsCount = 1

            endpoint {
                maxConnectionsPerRoute = 1
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
    }

    override fun clearNewPorts() {
        newPorts.clear()
    }

    private var lastRefresh: Long = 0

    override suspend fun internalDiscovery(eventsSink: EventsSink) : MutableList<Port<*>> {
        broadcastEvent(eventsSink, "AFORE discovery - DEV/hardcoded IP address $INVERTER_IP_ADDRESS")

        val result = ArrayList<Port<*>>()
        val portId = idBuilder.buildPortId(INVERTER_IP_ADDRESS, 0, "W")
        val portOperator = AforeWattageReadPortOperator(httpClient)
        val inverterPort = AforeWattagePort(portId, portOperator)
        ports.add(inverterPort)
        result.add(inverterPort)

        broadcastEvent(eventsSink, "AFORE discovery has finished")

        return result
    }

    private fun broadcastEvent(eventsSink: EventsSink, message: String) {
        val event = DiscoveryEventData(owningPluginId, message)
        eventsSink.broadcastEvent(event)
    }

    @Throws(Exception::class)
    override suspend fun refresh(now: Calendar) {
        if (now.timeInMillis - lastRefresh > 1000 * 30) {
            ports.forEach {
                val changed = it.refresh(now)
                if (changed) {
                    val event = PortUpdateEventData(owningPluginId, ADAPTER_ID, it)
                    operationSink?.broadcastEvent(event)
                }
            }

            lastRefresh = now.timeInMillis
        }
    }

    override fun executePendingChanges() {
    }

    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

    override fun stop() {
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.operationSink = operationSink
    }

    companion object {
        const val INVERTER_IP_ADDRESS = "192.168.1.103" //TODO: use settings when ready
        const val ADAPTER_ID = "0"
    }
}

