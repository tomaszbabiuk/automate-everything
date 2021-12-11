package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Humidity

class LuminosityValueBlockFactory : SimpleValueBlockFactory<Humidity>(
    Humidity::class.java,
    0.0,
    10000.0,
    0.0,
    "lux",
    "",
    null,
    CommonBlockCategories.Luminosity
)