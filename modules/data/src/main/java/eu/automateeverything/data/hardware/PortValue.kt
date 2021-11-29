package eu.automateeverything.data.hardware

import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

interface PortValue {
    fun toFormattedString() : Resource
    fun asDecimal() : BigDecimal
}

