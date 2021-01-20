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

abstract class Port<T,V: PortValue<T>>(
    val id: String,
    val canRead: Boolean,
    val canWrite: Boolean,
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
}

interface ReadPortOperator<V> {
    fun read() : V
}

interface WritePortOperator<V> {
    fun write(value : V)
    fun resetLatch()
    fun isLatchTriggered(): Boolean
}


class ConnectiblePort<T, V : PortValue<T>>(
    id : String,
    canRead: Boolean,
    canWrite: Boolean,
    readPortOperator: ReadPortOperator<V>?,
    writePortOperator: WritePortOperator<V>?)
    : Port<T, V>(id, canRead, canWrite, readPortOperator, writePortOperator), Connectible {

    override var lastSeen: Long = 0L
    override var connected: Boolean = false
    override var connectionLostInterval: Long = 0L

    constructor(id: String, readPortOperator: ReadPortOperator<V>, writePortOperator: WritePortOperator<V>)
            : this(id, true, true, readPortOperator, writePortOperator)

    constructor(id: String, readPortOperator: ReadPortOperator<V>)
            : this(id, true, false, readPortOperator, null)

    constructor(id: String, writePortOperator: WritePortOperator<V>)
            : this(id, false, true, null, writePortOperator)

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