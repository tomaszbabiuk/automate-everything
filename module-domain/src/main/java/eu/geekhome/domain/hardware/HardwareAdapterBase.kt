package eu.geekhome.domain.hardware

import eu.geekhome.domain.events.EventsSink
import java.util.*

abstract class HardwareAdapterBase : HardwareAdapter {

    override var state = AdapterState.Initialized
    override var lastDiscoveryTime = 0L
    override val id = "0"
    override var lastError: Throwable? = null

    abstract suspend fun internalDiscovery(eventsSink: EventsSink): MutableList<Port<*>>

    override suspend fun discover(discoverySink: EventsSink): MutableList<Port<*>> {
        lastDiscoveryTime = Calendar.getInstance().timeInMillis
        state = AdapterState.DiscoveryPending

        val ports = internalDiscovery( discoverySink)

        state = AdapterState.DiscoverySuccess
        return ports
    }
}