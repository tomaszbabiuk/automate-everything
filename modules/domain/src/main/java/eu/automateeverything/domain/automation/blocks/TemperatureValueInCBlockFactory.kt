package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Temperature

class TemperatureValueInCBlockFactory(val color: Int) : SimpleValueBlockFactory<Temperature>(
    Temperature::class.java,
    -273.15,
    10000.0,
    0.0,
    "°C",
    "_c",
    CelsiusToKelvinValueConverter(),
    CommonBlockCategories.Temperature
)