package eu.geekhome.domain.hardware

import java.io.InvalidClassException

class PortValueBuilder {
    companion object {
        fun <T : PortValue> buildFromDouble(valueClazz: Class<T>, x: Double): PortValue {
            if (valueClazz == Temperature::class.java) {
                return Temperature.fromDouble(x)
            }

            if (valueClazz == Humidity::class.java) {
                return Humidity.fromDouble(x)
            }

            if (valueClazz == Wattage::class.java) {
                return Wattage.fromDouble(x)
            }

            throw InvalidClassException("ValueClazz $valueClazz is not supported")
        }
    }
}