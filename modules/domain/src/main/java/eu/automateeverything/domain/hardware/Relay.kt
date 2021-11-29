package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class Relay(var value: BigDecimal) : PortValue {

    constructor(fromBoolean: Boolean) : this(
        if (fromBoolean) {
            BigDecimal.ONE
        } else {
            BigDecimal.ZERO
        }
    )

    private val on = Resource("On", "Wł")
    private val off = Resource("Off", "Wył")

    override fun toFormattedString(): Resource {
        return if (value == BigDecimal.ONE) on else off
    }

    override fun asDecimal(): BigDecimal {
        return value
    }

    override fun equals(other: Any?): Boolean {
        return (other is Relay) && other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        val ON = Relay(BigDecimal.ONE)
        val OFF = Relay(BigDecimal.ZERO)
    }
}