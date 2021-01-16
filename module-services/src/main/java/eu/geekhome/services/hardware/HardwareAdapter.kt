package eu.geekhome.services.hardware

import com.geekhome.common.OperationMode
import eu.geekhome.services.events.EventsSink
import java.util.*

interface HardwareAdapter {
    val id: String
    suspend fun discover(idBuilder: PortIdBuilder, eventsSink: EventsSink<String>) : MutableList<Port<*,*>>

    @Throws(Exception::class)
    fun refresh(now: Calendar)
    var state: AdapterState
    fun resetLatches()

    @Throws(Exception::class)
    fun reconfigure(operationMode: OperationMode)

    var lastDiscoveryTime: Long
    var lastError: Throwable?
}