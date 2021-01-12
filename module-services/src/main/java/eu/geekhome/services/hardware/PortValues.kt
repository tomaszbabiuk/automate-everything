package eu.geekhome.services.hardware

import com.geekhome.common.localization.Resource

sealed class PortValue<T>(val value: T) {
    abstract fun fromString(from: String) : T
    abstract fun toFormattedString() : Resource
}

class Binary(value: Boolean) : PortValue<Boolean>(value) {
    private val on = Resource("On/High", "Wł/Wysoki")
    private val off = Resource("Off/Low", "Wył/Niski")

    override fun fromString(from: String): Boolean {
        return from == "true"
    }

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }
}

class PowerLevel(value: Int) : PortValue<Int>(value) {
    override fun fromString(from: String): Int {
        return from.toInt()
    }

    override fun toFormattedString(): Resource {
        val multilingualValue = "$value %"
        return Resource(multilingualValue, multilingualValue)
    }
}

class Temperature(value: Double) : PortValue<Double>(value) {
    override fun fromString(from: String): Double {
        return from.toDouble()
    }

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f °C".format(value)
        return Resource(multilingualValue, multilingualValue)
    }
}

class Humidity(value: Double) : PortValue<Double>(value) {
    override fun fromString(from: String): Double {
        return from.toDouble()
    }

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f %".format(value)
        return Resource(multilingualValue, multilingualValue)
    }
}

class Wattage(value: Double) : PortValue<Double>(value) {
    override fun fromString(from: String): Double {
        return from.toDouble()
    }

    override fun toFormattedString(): Resource {
        val multilingualValue = "%.2f W".format(value)
        return Resource(multilingualValue, multilingualValue)
    }
}

