package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.ReadPortOperator

interface ShellyReadPortOperator<T> : ReadPortOperator<T> {
    val readTopic: String
    fun setValueFromMqttPayload(payload: String)
}

