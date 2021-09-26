package eu.automateeverything.domain.hardware

import eu.automateeverything.data.localization.Resource

class Relay(var value: Boolean) : PortValue {
    private val on = Resource("On", "Wł")
    private val off = Resource("Off", "Wył")

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }

    override fun asInteger(): Int {
        return if (value) 1 else 0
    }

    override fun asDouble(): Double {
        return if (value) 1.0 else 0.0
    }

    companion object {
        fun fromInteger(from: Int): Relay {
            return Relay(from == 1)
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other is Relay) && other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}