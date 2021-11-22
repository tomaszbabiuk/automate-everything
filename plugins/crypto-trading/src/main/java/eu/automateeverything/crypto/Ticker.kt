package eu.automateeverything.crypto

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.hardware.PortValue
import kotlin.math.roundToInt

class Ticker(
    private val price: Double,
) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f".format(price)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((price * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return price
    }
}