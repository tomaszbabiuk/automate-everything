package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.adapter.OneWireIOException
import com.dalsemi.onewire.adapter.USerialAdapter
import com.dalsemi.onewire.container.OneWireSensor
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PortUpdateEventData
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import eu.automateeverything.domain.hardware.PortIdBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.*

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPortName: String,
    private val eventsSink: EventsSink)
    : HardwareAdapterBase<OneWirePort<*>>() {

    private var mapper: OneWireSensorToPortMapper
    override val id: String = "1-WIRE $serialPortName"
    val channel = Channel<String>()

    init {
        val portIdBuilder = PortIdBuilder(owningPluginId)
        mapper = OneWireSensorToPortMapper(portIdBuilder)
    }

    var operationScope: CoroutineScope? = null
    var operationSink: EventsSink? = null


    override fun executePendingChanges() {
        //TODO
    }

    @Throws(OneWireException::class)
    private fun initializeAdapter(port: String): USerialAdapter {
        val adapter = USerialAdapter()
        adapter.selectPort(port)
        adapter.reset()
        adapter.beginExclusive(true)
        adapter.setSearchAllDevices()
        adapter.targetAllFamilies()
        adapter.speed = DSPortAdapter.SPEED_REGULAR
        return adapter
    }

    private fun freeAdapter(adapter: USerialAdapter) {
        adapter.endExclusive()
        adapter.freePort()
    }

    override fun start() {
        if (operationScope != null) {
            operationScope!!.cancel("Adapter already started")
        }

        //discovery
        val discoveryAdapter = initializeAdapter(serialPortName)
        searchForOneWireSensors(discoveryAdapter)
            .map(mapper::map)
            .forEach { ports[it.id] = it }
        freeAdapter(discoveryAdapter)

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                maintenanceLoop()
                delay(10000)
            }
        }
    }

    private fun searchForOneWireSensors(adapter: USerialAdapter): List<OneWireSensor> {
        return adapter
            .allDeviceContainers
            .toList()
            .filterIsInstance<OneWireSensor>()
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<OneWirePort<*>> {
        println("one-wire discovery... returned already cached data")

        channel.send("discovery")

        return ports.values.toList()
    }

    private fun maintenanceLoop() {
        val channelResult = channel.tryReceive()
        if (channelResult.isSuccess) {
            val order = channelResult.getOrNull()
            println(order)
        }

        val now = Calendar.getInstance()
        try {
            val adapter = initializeAdapter(serialPortName)
            ports
                .values
                .forEach {
                    val previousValue = it.read().asDouble()
                    val previousConnectionState = it.checkIfConnected(now)

                    it.refresh(now, adapter)

                    val valueHasChanged = it.read().asDouble() != previousValue
                    val connectionStateHasChanged = it.checkIfConnected(now) != previousConnectionState
                    if (valueHasChanged || connectionStateHasChanged) {
                        broadcastPortChangedEvent(it)
                    }
                }
            freeAdapter(adapter)
        } catch (ex: OneWireIOException) {
            ports.values.forEach {
                it.markDisconnected()
                broadcastPortChangedEvent(it)
            }
        }
    }

    private fun broadcastPortChangedEvent(it: OneWirePort<*>) {
        val event = PortUpdateEventData(owningPluginId, id, it)
        eventsSink.broadcastEvent(event)
    }
}
