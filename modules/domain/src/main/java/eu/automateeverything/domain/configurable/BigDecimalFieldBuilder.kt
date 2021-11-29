package eu.automateeverything.domain.configurable

import java.math.BigDecimal

class BigDecimalFieldBuilder : FieldBuilder<BigDecimal> {
    override fun fromPersistableString(value: String?): BigDecimal {
        if (value == null) {
            return BigDecimal.ZERO
        }

        return value.toBigDecimal()
    }

    override fun toPersistableString(value: BigDecimal): String {
        return value.toString()
    }
}