package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Temperature

class TemperatureValueInFBlockFactory: SimpleValueBlockFactory<Temperature>(
    Temperature::class.java,
    -273.15,
    10000.0,
    0.0,
    "°F",
    "_f",
    FahrenheitToKelvinValueConverter(),
    CommonBlockCategories.Temperature
)