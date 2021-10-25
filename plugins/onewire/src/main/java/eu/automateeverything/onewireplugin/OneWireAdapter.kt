package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.adapter.OneWireIOException
import com.dalsemi.onewire.adapter.USerialAdapter
import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.utils.Address
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PortUpdateEventData
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import eu.automateeverything.domain.hardware.PortIdBuilder
import kotlinx.coroutines.*
import java.util.*

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPortName: String,
    private val eventsSink: EventsSink)
    : HardwareAdapterBase<OneWirePort<*>>() {

    private var mapper: OneWireSensorToPortMapper
    override val id: String = "1-WIRE $serialPortName"

    init {
        val portIdBuilder = PortIdBuilder(owningPluginId)
        mapper = OneWireSensorToPortMapper(owningPluginId, portIdBuilder, eventsSink)
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
            .mapNotNull(mapper::map)
            .flatten()
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

    private fun searchForOneWireSensors(adapter: USerialAdapter): List<OneWireContainer> {
        return adapter
            .allDeviceContainers
            .toList()
            .filterIsInstance<OneWireContainer>()
    }

    override fun stop() {
        operationScope?.cancel("Stop called")
    }

    override suspend fun internalDiscovery(eventsSink: EventsSink): List<OneWirePort<*>> {
        eventsSink.broadcastDiscoveryEvent(owningPluginId, "The manual discovery of 1-wire adapters is disabled. Devices are discovered only once (on initial startup)")
        if (ports.values.isNotEmpty()) {
            eventsSink.broadcastDiscoveryEvent(owningPluginId, "Here's the list of already discovered devices:")
            ports.values.forEach {
                eventsSink.broadcastDiscoveryEvent(owningPluginId, Address.toString(it.oneWireAddress))
            }
        }

        return ports.values.toList()
    }

    private suspend fun maintenanceLoop() = coroutineScope {
        val now = Calendar.getInstance()
        try {
            val adapter = initializeAdapter(serialPortName)
            ports
                .values
                .forEach {
                    val previousValue = it.read().asDouble()
                    val previousConnectionState = it.checkIfConnected(now)

                    it.refresh(now, adapter) {  sleepingMs ->
                        var total = 0
                        while (isActive && total < sleepingMs) {
                            Thread.sleep(10)
                            total++
                        }
                    }

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
