package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.PortValue
import kotlin.math.roundToInt

class Ticker(
    private val price: Double,
    private val baseCurrency: String,
    private val counterCurrency: String,
) : PortValue {
    override fun toFormattedString(): Resource {
        return Resource.createUniResource("1 $baseCurrency = $price $counterCurrency")
    }

    override fun asInteger(): Int {
        return ((price * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return price
    }
}