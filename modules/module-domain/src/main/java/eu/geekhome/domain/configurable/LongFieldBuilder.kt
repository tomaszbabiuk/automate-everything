package eu.geekhome.domain.configurable

import java.lang.NumberFormatException

class LongFieldBuilder : FieldBuilder<Long?> {
    override fun fromPersistableString(value: String?): Long {
        return if (value == null) {
            0L
        } else try {
            value.toLong()
        } catch (nfe: NumberFormatException) {
            0L
        }
    }

    override fun toPersistableString(value: Long?): String {
        return value.toString()
    }
}