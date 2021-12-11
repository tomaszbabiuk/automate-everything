package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class Luminosity(var value: BigDecimal) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f lux".format(value)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asDecimal(): BigDecimal {
        return value
    }
}