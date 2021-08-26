package eu.geekhome.shellyplugin.ports

import eu.geekhome.domain.hardware.*
import java.util.*

open class ShellyPort<V: PortValue>(
    override val id : String,
    override val valueClazz: Class<V>,
    val sleepInterval: Long
) : IConnectible, Port<V> {
    final override var connectionValidUntil: Long = 0

    init {
        connectionValidUntil = Calendar.getInstance().timeInMillis + sleepInterval
    }
}


abstract class ShellyInputPort<V: PortValue>(
    id : String,
    valueClazz: Class<V>,
    sleepInterval: Long) : ShellyPort<V>(id, valueClazz, sleepInterval), InputPort<V> {

    abstract val readTopic: String
    abstract fun setValueFromMqttPayload(payload: String)
}

abstract class ShellyOutputPort<V: PortValue>(
    id : String,
    valueClazz: Class<V>,
    sleepInterval: Long) : ShellyInputPort<V>(id, valueClazz, sleepInterval), OutputPort<V> {

    abstract val writeTopic: String
    abstract fun getExecutePayload(): String?
}