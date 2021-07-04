package eu.geekhome.aforeplugin

import eu.geekhome.domain.events.DiscoveryEventData
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.PortIdBuilder
import eu.geekhome.domain.repository.SettingsDto
import eu.geekhome.domain.langateway.LanGateway
import eu.geekhome.domain.langateway.LanGatewayResolver
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.util.*

class AforeAdapter(
    private val owningPluginId: String,
    private val lanGatewayResolver: LanGatewayResolver) : HardwareAdapterBase<AforeWattageInputPort>() {

    var operationScope: CoroutineScope? = null
    private var operationSink: EventsSink? = null
    private val httpClient = createHttpClient()
    private val idBuilder = PortIdBuilder(owningPluginId, ADAPTER_ID)

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

    override suspend fun internalDiscovery(eventsSink: EventsSink): ArrayList<AforeWattageInputPort> = coroutineScope {
        val result = ArrayList<AforeWattageInputPort>()
        broadcastEvent(eventsSink, "Starting AFORE discovery")

        val lanGateways: List<LanGateway> = lanGatewayResolver.resolve()
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

        if (lanGateway != null) {
            val machineIPAddress = lanGateway.inet4Address
            val finder = AforeFinder(owningPluginId, httpClient, machineIPAddress)
            val discoveryJob = async { finder.searchForAforeDevices(eventsSink) }
            val aforeDevices = discoveryJob.await()

            aforeDevices.forEach {
                broadcastEvent(eventsSink, "AFORE inverter found, IP:${it.first}, s/n:${it.second}")
                val portId = idBuilder.buildPortId(it.second, 0, "W")
                val inverterPort = AforeWattageInputPort(portId, httpClient, it.first)
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

    private suspend fun maintenanceLoop(now: Calendar) {
        ports.values.forEach {
            val changed = it.refresh(now)
            if (changed) {
                val event = PortUpdateEventData(owningPluginId, ADAPTER_ID, it)
                operationSink?.broadcastEvent(event)
            }
        }
    }

    override fun executePendingChanges() {
        //This adapter is read-only
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override fun start(operationSink: EventsSink, settings: List<SettingsDto>) {
        this.operationSink = operationSink
        this.operationScope = CoroutineScope(Dispatchers.IO)

        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
            operationScope = null
        }

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                maintenanceLoop(Calendar.getInstance())
                delay(30000)
            }
        }
    }

    companion object {
        const val ADAPTER_ID = "0"
    }
}

