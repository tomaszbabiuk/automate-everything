package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.adapter.OneWireIOException
import com.dalsemi.onewire.adapter.USerialAdapter
import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.container.SwitchContainer
import com.dalsemi.onewire.container.TemperatureContainer
import com.dalsemi.onewire.utils.Address
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PortUpdateEventData
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.HardwareAdapterBase
import eu.automateeverything.domain.hardware.PortIdBuilder
import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.onewireplugin.helpers.SwitchContainerHelper
import eu.automateeverything.onewireplugin.helpers.TemperatureContainerHelper
import kotlinx.coroutines.*
import java.util.*

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPortName: String,
    private val eventsSink: EventsSink,
    ds2408AsRelays: List<String>
)
    : HardwareAdapterBase<OneWirePort<*>>() {

    private var mapper: OneWireSensorToPortMapper
    override val id: String = "1-WIRE $serialPortName"

    init {
        val portIdBuilder = PortIdBuilder(owningPluginId)
        mapper = OneWireSensorToPortMapper(owningPluginId, portIdBuilder, eventsSink, ds2408AsRelays)
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
        val adapterForDiscoveryOnly = initializeAdapter(serialPortName)
        val allContainers = searchForOneWireSensors(adapterForDiscoveryOnly)
        allContainers
            .mapNotNull(mapper::map)
            .flatten()
            .forEach { ports[it.id] = it }
        freeAdapter(adapterForDiscoveryOnly)

        operationScope = CoroutineScope(Dispatchers.IO)
        operationScope?.launch {
            while (isActive) {
                maintenanceLoop(allContainers)
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
            ports.values.forEach {
                eventsSink.broadcastDiscoveryEvent(owningPluginId, Address.toString(it.address))
            }
        }

        return ports.values.toList()
    }

    private suspend fun maintenanceLoop(allContainers: List<OneWireContainer>) = coroutineScope {
        val now = Calendar.getInstance().timeInMillis
        try {
            val adapter = initializeAdapter(serialPortName)
            allContainers.forEach { container ->
                println("Refreshing $container")
                if (container is TemperatureContainer) {
                    ports.values
                        .filter { port -> port.address.contentEquals(container.address) }
                        .filterIsInstance<OneWireTemperatureInputPort>()
                        .filter { port -> port.lastUpdateMs + 30000 < now }
                        .forEach { port ->
                            val newTemperatureK = TemperatureContainerHelper.read(adapter, container.address) + 273.15
                            //TODO: Handle exception
                            if (newTemperatureK != port.value.value) {
                                port.update(now, Temperature(newTemperatureK))
                                broadcastPortChangedEvent(port)
                            }
                        }
                }

                if (container is SwitchContainer) {
                    val readings = SwitchContainerHelper.read(container, true)

                    ports.values
                        .filter { port -> port.address.contentEquals(container.address) }
                        .filterIsInstance<OneWireBinaryInputPort>()
                        .forEach { port ->
                            val channel = port.channel
                            val reading = readings[channel]
                            val newBinaryReading = if (reading.isSensed) !port.value.value else reading.level
                            if (newBinaryReading != port.value.value) {
                                port.update(now, BinaryInput(newBinaryReading))
                                broadcastPortChangedEvent(port)
                            }
                        }
                }
            }

//            ports
//                .values
//                .forEach {
//                    val previousValue = it.read().asDouble()
//                    val previousConnectionState = it.checkIfConnected(now)
//
//                    it.refresh(now, adapter) {  sleepingMs ->
//                        var total = 0
//                        while (isActive && total < sleepingMs) {
//                            Thread.sleep(10)
//                            total++
//                        }
//                    }
//
//                    val valueHasChanged = it.read().asDouble() != previousValue
//                    val connectionStateHasChanged = it.checkIfConnected(now) != previousConnectionState
//                    if (valueHasChanged || connectionStateHasChanged) {
//                        broadcastPortChangedEvent(it)
//                    }
//                }
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
