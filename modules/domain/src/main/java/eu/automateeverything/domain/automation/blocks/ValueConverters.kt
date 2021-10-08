package eu.automateeverything.domain.automation.blocks

interface ValueConverter {
    fun convert(x: Double) : Double
}

class CelsiusToKelvinValueConverter : ValueConverter {
    override fun convert(x: Double): Double {
        return x + 273.15
    }
}

class FahrenheitToKelvinValueConverter : ValueConverter {
    override fun convert(x: Double): Double {
        return  (x + 459.67) * 5/9
    }
}