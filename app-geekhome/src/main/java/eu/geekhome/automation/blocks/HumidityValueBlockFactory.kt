package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.services.hardware.Humidity

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