package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.AdapterState
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventsSink

interface HardwareAdapter<T : Port<*>> {

    val ports: HashMap<String, T>
    fun clearNewPortsFlag()
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