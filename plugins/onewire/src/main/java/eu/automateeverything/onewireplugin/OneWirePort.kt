package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.data.hardware.PortValue
import java.util.*

abstract class OneWirePort<V: PortValue> : InputPort<V> {
    abstract val address: ByteArray
    abstract var value: V
    var lastUpdateMs: Long = Calendar.getInstance().timeInMillis
    override var connectionValidUntil = Long.MAX_VALUE

    override fun read(): V {
        return value
    }

    fun update(now: Long, value: V) {
        lastUpdateMs = now
        this.value = value
    }
}