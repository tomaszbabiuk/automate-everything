package eu.geekhome.aforeplugin

import eu.geekhome.domain.events.DiscoveryEventData
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.OperationMode
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.PortIdBuilder
import eu.geekhome.domain.repository.SettingsDto
import eu.geekhome.langateway.JavaLanGatewayResolver
import eu.geekhome.langateway.LanGateway
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
            maxConnectionsCount = 1000

            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 10000
                connectAttempts = 5
            }
        }
    }

    override fun clearNewPorts() {
        newPorts.clear()
    }

    private var lastRefresh: Long = 0

    override suspend fun internalDiscovery(eventsSink: EventsSink) : MutableList<Port<*>> = coroutineScope {
        broadcastEvent(eventsSink, "Starting AFORE discovery")

        val lanGateways: List<LanGateway> = JavaLanGatewayResolver().resolve()
        val lanGateway: LanGateway? = when (lanGateways.size) {
            0 -> {
                broadcastEvent(eventsSink, "Cannot resolve LAN gateway... aborting!")
                null
            }
            1 -> {
                lanGateways.first()
            }
            else -> {
                broadcastEvent(eventsSink, "There's more than one LAN gateways. Using the first one: ${lanGateways.first().interfaceName}!")
                lanGateways.first()
            }
        }

        val result = ArrayList<Port<*>>()
        if (lanGateway != null) {
            val machineIPAddress = lanGateway.inet4Address
            val finder = AforeFinder(owningPluginId, httpClient, machineIPAddress)
            val discoveryJob = async { finder.searchForAforeDevices(eventsSink) }
            val aforeDevices = discoveryJob.await()

            aforeDevices.forEach {
                broadcastEvent(eventsSink, "AFORE inverter found, IP:${it.first}, s/n:${it.second}")
                val portId = idBuilder.buildPortId(it.second, 0, "W")
                val portOperator = AforeWattageReadPortOperator(httpClient, it.first)
                val inverterPort = AforeWattagePort(portId, portOperator)
                ports.add(inverterPort)
                result.add(inverterPort)
            }
        }

        broadcastEvent(eventsSink, "AFORE discovery has finished")
        result
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
        const val ADAPTER_ID = "0"
    }
}

