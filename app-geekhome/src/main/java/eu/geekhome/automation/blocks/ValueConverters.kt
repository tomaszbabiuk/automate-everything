package eu.geekhome.automation.blocks

interface IValueConverter {
    fun convert(x: Double) : Double
}

class CelsiusToKelvinValueConverter : IValueConverter {
    override fun convert(x: Double): Double {
        return x - 273.15
    }
}

class FahrenheitToKelvinValueConverter : IValueConverter {
    override fun convert(x: Double): Double {
        return  (x + 459.67) * 5/9
    }
}