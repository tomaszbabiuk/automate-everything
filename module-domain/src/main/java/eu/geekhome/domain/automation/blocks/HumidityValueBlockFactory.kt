package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.R
import eu.geekhome.domain.hardware.Humidity

class HumidityValueBlockFactory(val color: Int) : SimpleValueBlockFactory<Humidity>(
    Humidity::class.java,
    0.0,
    100.0,
    0.0,
    "%",
    "",
    null,
    R.category_humidity,
    color
)