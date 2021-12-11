package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Humidity

class HumidityValueBlockFactory : SimpleValueBlockFactory<Humidity>(
    Humidity::class.java,
    0.0,
    100.0,
    0.0,
    "%",
    "",
    null,
    CommonBlockCategories.Humidity
)