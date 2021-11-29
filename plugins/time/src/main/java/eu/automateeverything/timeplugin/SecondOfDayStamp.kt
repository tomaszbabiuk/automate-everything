package eu.automateeverything.timeplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal

class SecondOfDayStamp(var value: BigDecimal) : PortValue {
    override fun toFormattedString(): Resource {
        return Resource.createUniResource(value.toString())
    }

    override fun asDecimal(): BigDecimal {
        return value
    }
}