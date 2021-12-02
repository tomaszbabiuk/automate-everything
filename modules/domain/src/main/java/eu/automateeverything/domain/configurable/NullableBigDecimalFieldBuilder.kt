package eu.automateeverything.domain.configurable

class NullableBigDecimalFieldBuilder : FieldBuilder<NullableBigDecimal> {
    override fun fromPersistableString(value: String?): NullableBigDecimal {
        if (value == null) {
            return NullableBigDecimal(null)
        }

        return NullableBigDecimal(value.toBigDecimal())
    }

    override fun toPersistableString(value: NullableBigDecimal): String {
        if (value.wrapped == null) {
            return ""
        }

        return value.wrapped.toString()
    }
}