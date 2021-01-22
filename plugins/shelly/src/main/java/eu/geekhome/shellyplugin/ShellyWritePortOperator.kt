package eu.geekhome.shellyplugin

import eu.geekhome.services.hardware.WritePortOperator

interface ShellyWritePortOperator<T> : WritePortOperator<T> {
    val writeTopic: String
    fun getExecutePayload(): String?
}

