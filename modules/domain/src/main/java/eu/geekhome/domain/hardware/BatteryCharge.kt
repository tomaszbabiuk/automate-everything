package eu.geekhome.domain.hardware

import eu.geekhome.data.localization.Resource
import kotlin.math.roundToInt

class BatteryCharge(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %%".format(value)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }
}