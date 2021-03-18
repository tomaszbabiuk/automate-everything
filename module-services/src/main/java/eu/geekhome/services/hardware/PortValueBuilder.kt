package eu.geekhome.services.hardware

import java.io.InvalidClassException

class PortValueBuilder {
    companion object {
        fun <T : PortValue> buildFromDouble(valueType: Class<T>, x: Double): PortValue {
            if (valueType == Temperature::class.java) {
                return Temperature.fromDouble(x)
            }

            if (valueType == Humidity::class.java) {
                return Humidity.fromDouble(x)
            }

            if (valueType == Wattage::class.java) {
                return Wattage.fromDouble(x)
            }

            throw InvalidClassException("ValueType $valueType is not supported")
        }
    }
}