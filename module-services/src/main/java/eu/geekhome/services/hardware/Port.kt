package eu.geekhome.services.hardware

import java.io.IOException

abstract class Port<T,V: PortValue<T>>(
    val id: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    private val readPortOperator: ReadPortOperator<V>? = null,
    private val writePortOperator: WritePortOperator<V>? = null
) {
    var isShadowed: Boolean = false; protected set
    var isOperational: Boolean = false; protected set
    var nonOperationalTime : Long = 0; protected set

    fun read() : V {
        if (canRead) {
            if (readPortOperator == null) {
                throw IOException("There's no read operator for this port")
            }

            return readPortOperator.read()
        }

        throw IOException("This port cannot read")
    }

    fun write(value : V) {
        if (canWrite) {
            if (writePortOperator == null) {
                throw IOException("There's no write operator for this port")
            }

            writePortOperator.write(value)
        } else {
            throw IOException("This port cannot write")
        }
    }
}

interface ReadPortOperator<V> {
    fun read() : V
}

interface WritePortOperator<V> {
    fun write(value : V)
    fun resetLatch()
    fun isLatchTriggered(): Boolean
}

class SynchronizedReadPortOperator<V>(private var value: V) : ReadPortOperator<V> {
    override fun read(): V {
        return value
    }

    fun sync(value: V) {
        this.value = value
    }
}

class WattageInPort(
        id: String,
        readPortOperator: ReadPortOperator<Wattage>) :
    Port<Double, Wattage>(id,true, false, readPortOperator, null)

class PowerLevelInOutPort(
        id: String,
        readPortOperator: ReadPortOperator<PowerLevel>,
        writePortOperator: WritePortOperator<PowerLevel>) :
    Port<Int, PowerLevel>(id, true, true, readPortOperator, writePortOperator)


class RelayInOutPort(
        id: String,
        readPortOperator: ReadPortOperator<Relay>,
        writePortOperator: WritePortOperator<Relay>) :
    Port<Boolean, Relay>(id, true, true, readPortOperator, writePortOperator)


