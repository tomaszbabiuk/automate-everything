package eu.geekhome.services.hardware

import com.geekhome.common.localization.Resource


sealed class PortValue<T>(initialValue: T) {

    private var internalValue: T = initialValue
    var isLatchTriggered = false
        private set

    var value: T
        get() = this.internalValue
        set(value) {
            this.isLatchTriggered = true
            this.internalValue = value
        }

    fun resetLatch() {
        isLatchTriggered = false
    }

    abstract fun fromString(from: String) : T
    abstract fun toFormattedString() : Resource
}

class BinaryInput(value: Boolean) : PortValue<Boolean>(value) {
    private val on = Resource("High", "Wysoki")
    private val off = Resource("Low", "Niski")

    override fun fromString(from: String): Boolean {
        return from == "true"
    }

    override fun toFormattedString(): Resource {
        return if (value) on else off
    }
}

class Relay(value: Boolean) : PortValue<Boolean>(value) {
    private val on = Resource("On", "Wł")
    private val off = Resource("Off", "Wył")

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

