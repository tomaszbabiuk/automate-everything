package eu.automateeverything.domain.hardware

import eu.automateeverything.data.localization.Resource
import kotlin.math.roundToInt

class Temperature(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value - 273.15)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }
}