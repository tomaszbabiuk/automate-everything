package eu.automateeverything.domain.hardware

import eu.automateeverything.data.localization.Resource

interface PortValue {
    fun toFormattedString() : Resource
    fun asInteger() : Int
    fun asDouble() : Double
}

