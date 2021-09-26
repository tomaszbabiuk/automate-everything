package eu.automateeverything.domain.hardware

import eu.automateeverything.data.localization.Resource

class PowerLevel(var value: Int) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "$value %"
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return value
    }

    override fun asDouble(): Double {
        return value.toDouble()
    }

    companion object {
        fun fromInteger(from: Int): PowerLevel {
            return PowerLevel(from)
        }
    }
}