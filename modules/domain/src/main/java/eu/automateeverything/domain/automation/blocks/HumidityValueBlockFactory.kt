package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Humidity

class HumidityValueBlockFactory(val color: Int) : SimpleValueBlockFactory<Humidity>(
    Humidity::class.java,
    0.0,
    100.0,
    0.0,
    "%",
    "",
    null,
    CommonBlockCategories.Humidity
)