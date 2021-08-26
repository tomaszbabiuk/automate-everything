package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.PortValue
import org.knowm.xchange.currency.CurrencyPair
import kotlin.math.roundToInt

class Ticker(
    private val price: Double,
    private val pair: CurrencyPair,
) : PortValue {
    override fun toFormattedString(): Resource {
        return Resource.createUniResource("1 ${pair.base.currencyCode} = $price ${pair.counter.currencyCode}")
    }

    override fun asInteger(): Int {
        return ((price * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return price
    }
}