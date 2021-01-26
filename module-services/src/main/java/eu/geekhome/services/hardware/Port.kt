package eu.geekhome.services.hardware

import java.io.IOException

interface Connectible {
    var lastSeen : Long
    var connected : Boolean

    var connectionLostInterval : Long

    fun markDisconnected() {
        connected = false
    }

    fun updateLastSeen(now : Long) {
        lastSeen = now
        connected = false
    }

    fun checkIfConnected(now: Long): Boolean {
        if (connected) {
            connected = now - lastSeen > connectionLostInterval
        }

        return connected
    }
}

abstract class Port<V: PortValue<*>>(
    val id: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val valueType: Class<V>,
    private val readPortOperator: ReadPortOperator<V>? = null,
    private val writePortOperator: WritePortOperator<V>? = null
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

    fun writeGeneral(value: String) {

    }
}

interface ReadPortOperator<V> {
    fun read() : V
}

interface WritePortOperator<V> {
    fun write(value : V)
    fun reset()
}

class ConnectiblePort<V : PortValue<*>>(
    id : String,
    canRead: Boolean,
    canWrite: Boolean,
    valueType: Class<V>,
    readPortOperator: ReadPortOperator<V>?,
    writePortOperator: WritePortOperator<V>?)
    : Port<V>(id, canRead, canWrite, valueType, readPortOperator, writePortOperator), Connectible {

    override var lastSeen: Long = 0L
    override var connected: Boolean = false
    override var connectionLostInterval: Long = 0L

    constructor(id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>, writePortOperator: WritePortOperator<V>)
            : this(id, true, true, valueType, readPortOperator, writePortOperator)

    constructor(id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>)
            : this(id, true, false, valueType, readPortOperator, null)

    constructor(id: String, valueType: Class<V>, writePortOperator: WritePortOperator<V>)
            : this(id, false, true, valueType, null, writePortOperator)

}