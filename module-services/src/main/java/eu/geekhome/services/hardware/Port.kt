package eu.geekhome.services.hardware

import java.io.IOException

abstract class Port<T,V: PortValue<T>>(
    val id: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    private val readOperator: ReadOperator<V>? = null,
    private val writeOperator: WriteOperator<V>? = null
) {
    var isShadowed: Boolean = false; protected set
    var isOperational: Boolean = false; protected set
    var nonOperationalTime : Long = 0; protected set

    fun read() : V {
        if (canRead) {
            if (readOperator == null) {
                throw IOException("There's no read operator for this port")
            }

            return readOperator.read()
        }

        throw IOException("This port cannot read")
    }

    fun write(value : V) {
        if (canWrite) {
            if (writeOperator == null) {
                throw IOException("There's no write operator for this port")
            }

            writeOperator.write(value)
        } else {
            throw IOException("This port cannot write")
        }
    }
}

interface ReadOperator<V> {
    fun read() : V
}

interface WriteOperator<V> {
    fun write(value : V)
}

class SynchronizedReadOperator<V>(private var value: V) : ReadOperator<V> {
    override fun read(): V {
        return value
    }

    fun sync(value: V) {
        this.value = value
    }
}

class WattageInPort(id: String, wattageValue: Double, readOperator: ReadOperator<Wattage>) :
    Port<Double, Wattage>(id,true, false, readOperator, null)
