package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Temperature

class TemperatureValueInKBlockFactory(val color: Int) : SimpleValueBlockFactory<Temperature>(
    Temperature::class.java,
    0.0,
    10000.0,
    273.15,
    "K",
    "_k",
    null,
    CommonBlockCategories.Temperature
)