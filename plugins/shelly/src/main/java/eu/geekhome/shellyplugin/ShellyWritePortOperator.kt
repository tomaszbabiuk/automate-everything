package eu.geekhome.shellyplugin

import eu.geekhome.domain.hardware.WritePortOperator

interface ShellyWritePortOperator<T> : WritePortOperator<T> {
    val writeTopic: String
    fun getExecutePayload(): String?
}

