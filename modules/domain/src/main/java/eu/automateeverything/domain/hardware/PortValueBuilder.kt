package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import java.io.InvalidClassException


class PortValueBuilder {
    companion object {
        fun <T : PortValue> buildFromDouble(valueClazz: Class<T>, x: Double): PortValue {

            valueClazz.constructors.forEach {
                if (it.parameters.size == 1 && it.parameters[0].type == Double::class.java) {
                    return it.newInstance(x) as PortValue
                }
            }

            throw InvalidClassException("ValueClazz $valueClazz should have at least one constructor with double parameter. Otherwise it cannot be constructed manually")
        }
    }
}