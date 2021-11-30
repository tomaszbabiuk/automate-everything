package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import java.io.InvalidClassException
import java.math.BigDecimal


class PortValueBuilder {
    companion object {
        fun <T : PortValue> buildFromDecimal(valueClazz: Class<in T>, x: BigDecimal): PortValue {

            valueClazz.constructors.forEach {
                if (it.parameters.size == 1 && it.parameters[0].type == BigDecimal::class.java) {
                    return it.newInstance(x) as PortValue
                }
            }

            throw InvalidClassException("ValueClazz $valueClazz should have at least one constructor with double parameter. Otherwise it cannot be constructed manually")
        }
    }
}