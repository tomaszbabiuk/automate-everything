package eu.geekhome.automation.blocks

import eu.geekhome.R
import eu.geekhome.domain.hardware.Temperature

class TemperatureValueInKBlockFactory(val color: Int) : SimpleValueBlockFactory<Temperature>(
    Temperature::class.java,
    0.0,
    10000.0,
    273.15,
    "K",
    "_k",
    null,
    R.category_temperature,
    color
)