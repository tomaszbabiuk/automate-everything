package eu.geekhome.domain.configurable

import java.lang.NumberFormatException

class DoubleFieldBuilder : FieldBuilder<Double> {
    override fun fromPersistableString(value: String?): Double {
        return if (value == null) {
            0.0
        } else try {
            value.toDouble()
        } catch (nfe: NumberFormatException) {
            0.0
        }
    }

    override fun toPersistableString(value: Double): String {
        return value.toString()
    }
}