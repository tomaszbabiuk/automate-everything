package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class BinaryInput(var value: Boolean) : PortValue {
    private val on = Resource("High", "Wysoki")
    private val off = Resource("Low", "Niski")

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }

    override fun asDecimal(): BigDecimal {
        return if (value) BigDecimal.ONE else BigDecimal.ZERO
    }
}