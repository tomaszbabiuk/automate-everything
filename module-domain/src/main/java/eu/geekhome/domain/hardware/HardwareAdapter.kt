package eu.geekhome.domain.hardware

import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.repository.SettingsDto

interface HardwareAdapter {

    val ports: List<Port<*>>
    fun clearNewPorts()
    fun hasNewPorts(): Boolean
    val id: String
    suspend fun discover(discoverySink: EventsSink)

    var state: AdapterState
    fun executePendingChanges()

    fun stop()
    fun start(operationSink: EventsSink, settings: List<SettingsDto>)

    var lastDiscoveryTime: Long
    var lastError: Throwable?
}