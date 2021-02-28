package eu.geekhome.services.hardware

import eu.geekhome.services.events.EventsSink
import java.util.*

abstract class HardwareAdapterBase : HardwareAdapter {

    override var state = AdapterState.Initialized
    override var lastDiscoveryTime = 0L
    override val id = "0"
    override var lastError: Throwable? = null

    abstract suspend fun internalDiscovery(idBuilder: PortIdBuilder, eventsSink: EventsSink<HardwareEvent>): MutableList<Port<*>>

    override suspend fun discover(idBuilder: PortIdBuilder, eventsSink: EventsSink<HardwareEvent>): MutableList<Port<*>> {
        lastDiscoveryTime = Calendar.getInstance().timeInMillis
        state = AdapterState.DiscoveryPending

        val ports = internalDiscovery(idBuilder, eventsSink)

        state = AdapterState.DiscoverySuccess
        return ports
    }
}