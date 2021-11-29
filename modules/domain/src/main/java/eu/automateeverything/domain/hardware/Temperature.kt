package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class Temperature(var value: BigDecimal) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value - 273.15.toBigDecimal())
        return Resource.createUniResource(multilingualValue)
    }

    override fun asDecimal(): BigDecimal {
        return value
    }
}