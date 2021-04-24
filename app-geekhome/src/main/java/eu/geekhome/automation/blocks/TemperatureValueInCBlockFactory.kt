package eu.geekhome.automation.blocks

import eu.geekhome.R
import eu.geekhome.services.hardware.Temperature

class TemperatureValueInCBlockFactory(val color: Int) : SimpleValueBlockFactory<Temperature>(
    Temperature::class.java,
    -273.15,
    10000.0,
    0.0,
    "°C",
    "_c",
    CelsiusToKelvinValueConverter(),
    R.category_temperature,
    color
)