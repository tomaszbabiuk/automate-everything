package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.WritePortOperator

interface ShellyOutPortOperator<T> : WritePortOperator<T> {
    val writeTopic: String
    fun convertValueToMqttPayload(): String
}

