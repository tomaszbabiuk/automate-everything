package eu.automateeverything.onewireplugin

import com.dalsemi.onewire.OneWireException
import com.dalsemi.onewire.adapter.DSPortAdapter
import com.dalsemi.onewire.adapter.OneWireIOException
import com.dalsemi.onewire.adapter.USerialAdapter
import com.dalsemi.onewire.container.OneWireContainer
import com.dalsemi.onewire.container.SwitchContainer
import com.dalsemi.onewire.container.TemperatureContainer
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
import java.util.concurrent.ConcurrentLinkedQueue

class OneWireAdapter(
    private val owningPluginId: String,
    private val serialPortName: String,
    private val eventsSink: EventsSink,
    private val ds2408AsRelays: List<String>
)
    : HardwareAdapterBase<OneWirePort<*>>() {

    private var mapper: OneWireSensorToPortMapper
    override val id: String = "1-WIRE $serialPortName"

    private val executionQueue = ConcurrentLinkedQueue<ValueSnapshot>()

    init {
        val portIdBuilder = PortIdBuilder(owningPluginId)
        mapper = OneWireSensorToPortMapper(owningPluginId, portIdBuilder, eventsSink, ds2408AsRelays)
    }

    var operationScope: CoroutineScope? = null
    var operationSink: EventsSink? = null


    override fun executePendingChanges() {
        val portsGroupedByAddress = ports
            .values
            .filterIsInstance<OneWireRelayPort>()
            .groupBy { it.address }

        portsGroupedByAddress.forEach { (address, u) ->
            val channels = u.size
            val sortedPorts = u.sortedBy { it.channel }

            val prevValues = Array(channels) { i ->
                sortedPorts[i].value.value
            }
            val newValues = Array(channels) { i ->
                sortedPorts[i].requestedValue?.value ?: sortedPorts[i].value.value
            }

            val changedItems = newValues.filterIndexed { index, newValue -> prevValues[index] != newValue }
            if (changedItems.isNotEmpty()) {
                println("Changed! $address")
                val valueSnapshot = ValueSnapshot(address, prevValues, newValues)
                executionQueue.add(valueSnapshot)
            }
        }
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
                maintenanceLoop()
                delay(100)
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

        return ports.values.toList()
    }

    private suspend fun maintenanceLoop() = coroutineScope {
        val now = Calendar.getInstance()
        try {
            val adapter = initializeAdapter(serialPortName)

            maintainExecutionOfRelays(adapter)

            val discoveredAddresses = ports.values.map { it.address }.distinct()
            discoveredAddresses.forEach {
                if (executionQueue.isEmpty()) {
                    val container = adapter.getDeviceContainer(it)
                    maintainAsThermometer(container, now)
                    maintainAsBinaryInput(container, now)
                }
            }

            freeAdapter(adapter)
        }
        catch (ex: OneWireIOException) {
            println(ex)
            ports.values.forEach {
                maintainPortConnectivity(now, it, false)
                broadcastPortChangedEvent(it)
            }
        }
    }

    private suspend fun maintainExecutionOfRelays(adapter: USerialAdapter) {
        if (!executionQueue.isEmpty()) {
            val snapshot = executionQueue.poll()
            val container = adapter.getDeviceContainer(snapshot.containerAddress) as SwitchContainer
            val newValuesInverted = Array(snapshot.newValues.size) {
                !snapshot.newValues[it]
            }
            SwitchContainerHelper.execute(container, newValuesInverted)

            ports
                .values
                .filter { port -> port.address.contentEquals(snapshot.containerAddress) }
                .filterIsInstance<OneWireRelayPort>()
                .forEach { port ->
                    val oldValue = port.value.value
                    port.commit()
                    val newValue = port.value.value
                    if (newValue != oldValue) {
                        broadcastPortChangedEvent(port)
                    }
                }
        }
    }

    private fun maintainAsThermometer(container: OneWireContainer?, now: Calendar) {
        if (container is TemperatureContainer) {
            ports.values
                .filter { port -> port.address.contentEquals(container.address) }
                .map { port ->
                    maintainPortConnectivity(now, port, true)
                    port
                }
                .filterIsInstance<OneWireTemperatureInputPort>()
                .filter { port -> port.lastUpdateMs + 30000 < now.timeInMillis }
                .forEach { port ->
                    val newTemperatureK = TemperatureContainerHelper.read(container) + 273.15
                    if (newTemperatureK != port.value.value) {
                        port.update(now.timeInMillis, Temperature(newTemperatureK))
                        broadcastPortChangedEvent(port)
                    }
                }
        }
    }

    private fun maintainAsBinaryInput(container: OneWireContainer?, now: Calendar) {
        if (container is SwitchContainer) {
            val isRelay = ds2408AsRelays.contains(container.addressAsString)
            if (isRelay) {
                ports.values
                    .filter { port -> port.address.contentEquals(container.address) }
                    .map { port ->
                        maintainPortConnectivity(now, port, true)
                        port
                    }
            } else {
                val readings = SwitchContainerHelper.read(container, true)

                ports.values
                    .filter { port -> port.address.contentEquals(container.address) }
                    .map { port ->
                        maintainPortConnectivity(now, port, true)
                        port
                    }
                    .filterIsInstance<OneWireBinaryInputPort>()
                    .forEach { port ->
                        val channel = port.channel
                        val reading = readings[channel]
                        val newBinaryReading = if (reading.isSensed) !port.value.value else reading.level
                        if (newBinaryReading != port.value.value) {
                            port.update(now.timeInMillis, BinaryInput(newBinaryReading))
                            broadcastPortChangedEvent(port)
                        }
                    }
            }
        }
    }

    private fun maintainPortConnectivity(now: Calendar, port: OneWirePort<*>, connected: Boolean) {
        val oldConnectionState = port.checkIfConnected(now)
        if (connected) {
            port.connectionValidUntil = Long.MAX_VALUE
        } else {
            port.markDisconnected()
        }
        val newConnectionState = port.checkIfConnected(now)
        if (oldConnectionState != newConnectionState) {
            broadcastPortChangedEvent(port)
        }
    }

    private fun broadcastPortChangedEvent(it: OneWirePort<*>) {
        val event = PortUpdateEventData(owningPluginId, id, it)
        eventsSink.broadcastEvent(event)
    }

    data class ValueSnapshot(
        val containerAddress: ByteArray,
        val previousValues: Array<Boolean>,
        val newValues: Array<Boolean>) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ValueSnapshot

            if (!containerAddress.contentEquals(other.containerAddress)) return false
            if (!previousValues.contentEquals(other.previousValues)) return false
            if (!newValues.contentEquals(other.newValues)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = containerAddress.contentHashCode()
            result = 31 * result + previousValues.contentHashCode()
            result = 31 * result + newValues.contentHashCode()
            return result
        }
    }
}
