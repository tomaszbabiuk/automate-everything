package eu.geekhome.domain.hardware

import eu.geekhome.data.localization.Resource

interface PortValue {
    fun toFormattedString() : Resource
    fun asInteger() : Int
    fun asDouble() : Double
}

