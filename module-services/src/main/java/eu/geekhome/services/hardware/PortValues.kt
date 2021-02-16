package eu.geekhome.services.hardware

import eu.geekhome.services.localization.Resource
import kotlin.math.roundToInt


sealed class PortValue<T>(var value: T) {
    abstract fun toFormattedString() : Resource
    abstract fun toInteger() : Int
}

class BinaryInput(value: Boolean) : PortValue<Boolean>(value) {
    private val on = Resource("High", "Wysoki")
    private val off = Resource("Low", "Niski")

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }

    override fun toInteger(): Int {
        return if (value) 1 else 0
    }

    companion object {
        fun fromString(from: String): BinaryInput {
            return BinaryInput(from == "true")
        }
    }
}

class Relay(value: Boolean) : PortValue<Boolean>(value) {
    private val on = Resource("On", "Wł")
    private val off = Resource("Off", "Wył")

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }

    override fun toInteger(): Int {
        return if (value) 1 else 0
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

class PowerLevel(value: Int) : PortValue<Int>(value) {

    override fun toFormattedString(): Resource {
        val multilingualValue = "$value %"
        return Resource(multilingualValue, multilingualValue)
    }

    override fun toInteger(): Int {
        return value
    }

    companion object {
        fun fromInteger(from: Int): PowerLevel {
            return PowerLevel(from)
        }
    }
}

class Temperature(value: Double) : PortValue<Double>(value) {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun toInteger(): Int {
        return ((value * 100).roundToInt())
    }

    companion object {
        fun fromString(from: String): Temperature {
            return Temperature(from.toDouble())
        }
    }
}

class Humidity(value: Double) : PortValue<Double>(value) {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun toInteger(): Int {
        return ((value * 100).roundToInt())
    }

    companion object {
        fun fromString(from: String): Humidity {
            return Humidity(from.toDouble())
        }
    }
}

class Wattage(value: Double) : PortValue<Double>(value) {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f W".format(value)
        return Resource(multilingualValue, multilingualValue)
    }

    override fun toInteger(): Int {
        return ((value * 100).roundToInt())
    }

    companion object {
        fun fromString(from: String): Wattage {
            return Wattage(from.toDouble())
        }
    }
}

