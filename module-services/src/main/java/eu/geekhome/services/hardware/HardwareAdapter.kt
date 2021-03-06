package eu.geekhome.services.hardware

import eu.geekhome.services.events.EventsSink
import java.util.*

interface HardwareAdapter {

    val newPorts: List<Port<*>>
    fun clearNewPorts()
    val id: String
    suspend fun discover(eventsSink: EventsSink<HardwareEvent>) : MutableList<Port<*>>

    @Throws(Exception::class)
    fun refresh(now: Calendar)
    var state: AdapterState
    fun executePendingChanges()

    @Throws(Exception::class)
    fun reconfigure(operationMode: OperationMode)
    fun stop()
    fun start(updateSink: EventsSink<PortUpdateEvent>)

    var lastDiscoveryTime: Long
    var lastError: Throwable?
}