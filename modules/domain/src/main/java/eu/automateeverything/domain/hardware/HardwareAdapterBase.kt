package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.AdapterState
import eu.automateeverything.domain.events.EventsSink
import java.util.*
import kotlin.collections.HashMap

abstract class HardwareAdapterBase<T : Port<*>> : HardwareAdapter<T> {

    override var state = AdapterState.Initialized
    override var lastDiscoveryTime = 0L
    override var lastError: Throwable? = null
    override val ports: LinkedHashMap<String, T> = LinkedHashMap()
    private var hasNewPorts: Boolean = false

    override fun clearNewPortsFlag() {
        hasNewPorts = false
    }

    override fun hasNewPorts(): Boolean {
        return hasNewPorts
    }
    abstract suspend fun internalDiscovery(eventsSink: EventsSink) : List<T>

    override suspend fun discover(discoverySink: EventsSink) {
        lastDiscoveryTime = Calendar.getInstance().timeInMillis
        state = AdapterState.Discovery

        val freshlyDiscoveredPorts = internalDiscovery(discoverySink)
        addPotentialNewPorts(freshlyDiscoveredPorts)

        state = AdapterState.Operating
    }

    protected fun addPotentialNewPorts(newPorts: List<T>) {
        newPorts.forEach {
            if (!ports.containsKey(it.id)) {
                ports[it.id] = it
                hasNewPorts = true
            }
        }
    }
}