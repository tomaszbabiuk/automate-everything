package eu.geekhome.domain.hardware

import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.repository.SettingsDto
import java.util.*

interface HardwareAdapter {

    val newPorts: List<Port<*>>
    fun clearNewPorts()
    val id: String
    suspend fun discover(discoverySink: EventsSink) : MutableList<Port<*>>

    @Throws(Exception::class)
    suspend fun refresh(now: Calendar)
    var state: AdapterState
    fun executePendingChanges()

    @Throws(Exception::class)
    fun reconfigure(operationMode: OperationMode)
    fun stop()
    fun start(operationSink: EventsSink, settings: List<SettingsDto>)

    var lastDiscoveryTime: Long
    var lastError: Throwable?
}