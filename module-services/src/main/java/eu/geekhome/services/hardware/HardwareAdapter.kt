package eu.geekhome.services.hardware

import com.geekhome.common.OperationMode
import eu.geekhome.services.events.EventsSink
import java.util.*

interface HardwareAdapter {
    val id: String
    suspend fun discover(idBuilder: PortIdBuilder, eventsSink: EventsSink<String>) : MutableList<Port<*,*>>
    fun canBeRediscovered(): Boolean

    @Throws(Exception::class)
    fun refresh(now: Calendar)
    val state: AdapterState
    fun resetLatches()

    @Throws(Exception::class)
    fun reconfigure(operationMode: OperationMode)
    val lastDiscoveryTime: Long
    val lastError: Throwable?
}