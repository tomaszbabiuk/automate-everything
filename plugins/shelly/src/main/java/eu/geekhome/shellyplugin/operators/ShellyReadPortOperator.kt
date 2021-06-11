package eu.geekhome.shellyplugin.operators

import eu.geekhome.domain.hardware.ReadPortOperator

interface ShellyReadPortOperator<T> : ReadPortOperator<T> {
    val readTopic: String
    fun setValueFromMqttPayload(payload: String)
}

