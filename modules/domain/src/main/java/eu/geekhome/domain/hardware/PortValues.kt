package eu.geekhome.domain.hardware

import eu.geekhome.data.localization.Resource
import kotlin.math.roundToInt

interface PortValue {
    fun toFormattedString() : Resource
    fun asInteger() : Int
    fun asDouble() : Double
}

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

class PowerLevel(var value: Int) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "$value %"
        return Resource.createUniResource(multilingualValue)
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

class BatteryCharge(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %%".format(value)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }

    companion object {
        fun fromString(from: String): BatteryCharge {
            return BatteryCharge(from.toDouble())
        }
    }
}

class Temperature(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value - 273.15)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }

    companion object {
        fun fromDouble(from: Double): Temperature {
            return Temperature(from)
        }
    }
}

class Humidity(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %%".format(value)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }

    companion object {
        fun fromDouble(from: Double): Humidity {
            return Humidity(from)
        }
    }
}

class Wattage(var value: Double) : PortValue {

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f W".format(value)
        return Resource.createUniResource(multilingualValue)
    }

    override fun asInteger(): Int {
        return ((value * 100).roundToInt())
    }

    override fun asDouble(): Double {
        return value
    }

    companion object {
        fun fromDouble(from: Double): Wattage {
            return Wattage(from)
        }
    }
}

