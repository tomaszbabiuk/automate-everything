package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.ReadPortOperator

interface ShellyInPortOperator<T> : ReadPortOperator<T> {
    val readTopic: String
    fun setValueFromMqttPayload(payload: String)
}

