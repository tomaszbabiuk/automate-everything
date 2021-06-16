package eu.geekhome.aforeplugin

import eu.geekhome.domain.events.DiscoveryEventData
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PortUpdateEventData
import eu.geekhome.domain.hardware.HardwareAdapterBase
import eu.geekhome.domain.hardware.OperationMode
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.PortIdBuilder
import eu.geekhome.domain.repository.SettingsDto
import eu.geekhome.landiscoverysettings.LanDiscoverySettings
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.net.InetAddress
import java.util.*

class AforeAdapter(private val owningPluginId: String) : HardwareAdapterBase() {

    private var from: InetAddress? = null
    private var to: InetAddress? = null
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
                connectTimeout = 5000
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

        val result = ArrayList<Port<*>>()
        if (from != null && to != null) {
            val finder = AforeFinder(owningPluginId, httpClient, from!!, to!!)

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
        } else {
            broadcastEvent(eventsSink, "AFORE plugin settings are incorrect - abandon!")
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

        try {
            val lanDiscoverySettings = settings.first { it.clazz == LanDiscoverySettings::class.java.name }
            from = InetAddress.getByName(lanDiscoverySettings.fields[LanDiscoverySettings.FIELD_IP_FROM])
            to = InetAddress.getByName(lanDiscoverySettings.fields[LanDiscoverySettings.FIELD_IP_TO])
        } catch (ex: Exception) {
            broadcastEvent(operationSink, "Afore plugin settings seems to be invalid!")
        }
    }

    companion object {
        const val ADAPTER_ID = "0"
    }
}

