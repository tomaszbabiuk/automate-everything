package eu.automateeverything.domain.automation.blocks

import java.math.BigDecimal

interface ValueConverter {
    fun convert(x: BigDecimal) : BigDecimal
}

class CelsiusToKelvinValueConverter : ValueConverter {
    override fun convert(x: BigDecimal): BigDecimal {
        return x + 273.15.toBigDecimal()
    }
}

class FahrenheitToKelvinValueConverter : ValueConverter {
    override fun convert(x: BigDecimal): BigDecimal {
        return  (x + 459.67.toBigDecimal()) * 5.0.toBigDecimal()/9.0.toBigDecimal()
    }
}