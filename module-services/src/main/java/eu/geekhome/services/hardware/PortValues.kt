package eu.geekhome.services.hardware

import eu.geekhome.services.localization.Resource
import kotlin.math.roundToInt



sealed class PortValue {
    abstract fun toFormattedString() : Resource
    abstract fun asInteger() : Int
    abstract fun asDouble() : Double
}

class BinaryInput(var value: Boolean) : PortValue() {
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

class Relay(var value: Boolean) : PortValue() {
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
}

class PowerLevel(var value: Int) : PortValue() {

    override fun toFormattedString(): Resource {
        val multilingualValue = "$value %"
        return Resource(multilingualValue, multilingualValue)
    }

    override fun asInteger(): Int {
        return value
    }

    override fun asDouble(): Double {
        return value.toDouble()
    }

    companion object {
        fun fromInteger(from: Int): PowerLevel {
            return PowerLevel(from)
        }
    }
}

class Temperature(var value: Double) : PortValue() {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }

    companion object {
        fun fromString(from: String): Temperature {
            return Temperature(from.toDouble())
        }
    }
}

class Humidity(var value: Double) : PortValue() {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }
}

class Wattage(var value: Double) : PortValue() {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f W".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }
}

