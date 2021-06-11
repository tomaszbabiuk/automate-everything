package eu.geekhome.shellyplugin

import eu.geekhome.domain.hardware.ConnectiblePort
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.hardware.ReadPortOperator
import eu.geekhome.domain.hardware.WritePortOperator
import java.util.*

class ShellyPort<V: PortValue>(
    val sleepInterval: Long,
    id : String,
    canRead: Boolean,
    canWrite: Boolean,
    valueType: Class<V>,
    readPortOperator: ReadPortOperator<V>?,
    writePortOperator: WritePortOperator<V>?) : ConnectiblePort<V>(id, canRead, canWrite, valueType, readPortOperator, writePortOperator) {

    init {
        connectionValidUntil = Calendar.getInstance().timeInMillis + sleepInterval
    }

    constructor(sleepInterval: Long, id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>, writePortOperator: WritePortOperator<V>)
            : this(sleepInterval, id, true, true, valueType, readPortOperator, writePortOperator)

    constructor(sleepInterval: Long, id: String, valueType: Class<V>, readPortOperator: ReadPortOperator<V>)
            : this(sleepInterval, id, true, false, valueType, readPortOperator, null)

}