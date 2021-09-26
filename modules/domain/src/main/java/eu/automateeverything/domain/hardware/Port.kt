package eu.automateeverything.domain.hardware

import java.util.*

interface IConnectible {
    var connectionValidUntil : Long

    fun updateValidUntil(until : Long) {
        connectionValidUntil = until
    }

    fun markDisconnected() {
        connectionValidUntil = 0
    }

    fun checkIfConnected(now: Calendar): Boolean {
        return now.timeInMillis < connectionValidUntil
    }
}

interface Port<V: PortValue> : IConnectible {
    val id: String

    val valueClazz: Class<V>

    val canRead: Boolean
        get() = this is InputPort<V>

    val canWrite: Boolean
        get() = this is OutputPort<V>

    fun tryRead() : V? {
        return (this as InputPort<V>).read()
    }

    fun tryWrite(value: V) {
        return (this as OutputPort<V>).write(value)
    }
}

interface InputPort<V : PortValue> : Port<V> {
    fun read() : V
}

interface OutputPort<V : PortValue> : InputPort<V> {
    fun write(value : V)
    val requestedValue : V?
    fun reset()
}