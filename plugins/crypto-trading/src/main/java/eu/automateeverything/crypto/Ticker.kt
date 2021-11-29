package eu.automateeverything.crypto

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal

class Ticker(
    private val price: BigDecimal,
) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f".format(price)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asDecimal(): BigDecimal {
        return price
    }
}