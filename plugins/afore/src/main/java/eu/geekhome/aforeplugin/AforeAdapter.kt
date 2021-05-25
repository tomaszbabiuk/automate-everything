package eu.geekhome.aforeplugin

import eu.geekhome.aforeplugin.AforeAdapter.Companion.INVERTER_IP_ADDRESS
import eu.geekhome.aforeplugin.AforeAdapterFactory.Companion.FACTORY_ID
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.DiscoveryEventData
import eu.geekhome.services.events.PortUpdateEventData
import eu.geekhome.services.hardware.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import java.util.*
import kotlin.collections.ArrayList

class AforeAdapter : HardwareAdapterBase() {

    private var operationSink: EventsSink? = null
    override val newPorts = ArrayList<Port<*>>()
    private val httpClient = createHttpClient()
    private val idBuilder = PortIdBuilder(FACTORY_ID, ADAPTER_ID)
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
        val event = DiscoveryEventData(FACTORY_ID, message)
        eventsSink.broadcastEvent(event)
    }

    @Throws(Exception::class)
    override suspend fun refresh(now: Calendar) {
        if (now.timeInMillis - lastRefresh > 30000) {
            //TODO: recheck if ktor is busy
            ports.forEach {
                val changed = it.refresh()
                if (changed) {
                    val event = PortUpdateEventData(FACTORY_ID, ADAPTER_ID, it)
                    operationSink?.broadcastEvent(event)
                }
            }

            lastRefresh = now.timeInMillis
        }
    }

    override suspend fun executePendingChanges() {
    }

    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

    override fun stop() {
    }

    override fun start(operationSink: EventsSink) {
        this.operationSink = operationSink
    }

    companion object {
        const val INVERTER_IP_ADDRESS = "192.168.1.103"
        const val ADAPTER_ID = "0"
    }
}

class AforeWattagePort(id: String, portOperator: AforeWattageReadPortOperator) : ConnectiblePort<Wattage>(id, Wattage::class.java, portOperator) {
    init {
        this.connectionValidUntil = Calendar.getInstance().timeInMillis + 1000 * 60 * 10 //now + 10 minutes
    }

    suspend fun refresh(): Boolean {
        try {
            val aforeOperator = readPortOperator as AforeWattageReadPortOperator
            return aforeOperator.refresh()
        } catch (ex: Exception) {
            cancelValidity()
        }

        return false
    }
}

class AforeWattageReadPortOperator(
    private val httpClient: HttpClient) : ReadPortOperator<Wattage> {

    private var cachedValue = Wattage(0.0)

    override fun read(): Wattage {
        return cachedValue
    }

    suspend fun refresh() : Boolean {
        val newValue = readInverterPower()
        if (cachedValue.value != newValue) {
            cachedValue = Wattage(newValue)
            return true
        }

        return false
    }

    private suspend fun readInverterPower(): Double {
        val inverterResponse = httpClient.get<String>("http://$INVERTER_IP_ADDRESS/status.html")
        val lines = inverterResponse.split(";").toTypedArray()
        for (line in lines) {
            if (line.contains("webdata_now_p")) {
                val s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                return java.lang.Double.valueOf(s)
            }
        }

        return 0.0
    }

}