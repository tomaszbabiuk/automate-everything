package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource

class BinaryInput(var value: Boolean) : PortValue {
    private val on = Resource("High", "Wysoki")
    private val off = Resource("Low", "Niski")

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }

    override fun asInteger(): Int {
        return if (value) 1 else 0
    }

    override fun asDouble(): Double {
        return if (value) 1.0 else 0.0
    }
}