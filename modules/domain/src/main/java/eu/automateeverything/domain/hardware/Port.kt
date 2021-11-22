package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue

interface Port<V: PortValue> : Connectible {
    val id: String

    val valueClazz: Class<V>

    val canRead: Boolean
        get() = this is InputPort<V>

    val canWrite: Boolean
        get() = this is OutputPort<V>

    fun tryRead() : V? {
        return (this as InputPort<V>).read()
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