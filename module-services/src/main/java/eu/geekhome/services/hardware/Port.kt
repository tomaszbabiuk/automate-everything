package eu.geekhome.services.hardware

import java.io.IOException
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

abstract class Port<V: PortValue>(
    val id: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val valueType: Class<V>,
    val readPortOperator: ReadPortOperator<V>? = null,
    val writePortOperator: WritePortOperator<V>? = null
) {
    var isShadowed: Boolean = false; protected set

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
    fun reset()
}

open class ConnectiblePort<V : PortValue>(
    id : String,
    canRead: Boolean,
    canWrite: Boolean,
    valueType: Class<V>,
    readPortOperator: ReadPortOperator<V>?,
    writePortOperator: WritePortOperator<V>?)
    : Port<V>(id, canRead, canWrite, valueType, readPortOperator, writePortOperator), IConnectible {

    override var connectionValidUntil: Long = 0L

    constructor(id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>, writePortOperator: WritePortOperator<V>)
            : this(id, true, true, valueType, readPortOperator, writePortOperator)

    constructor(id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>)
            : this(id, true, false, valueType, readPortOperator, null)

    protected fun cancelValidity() {
        connectionValidUntil = 0L
    }
}