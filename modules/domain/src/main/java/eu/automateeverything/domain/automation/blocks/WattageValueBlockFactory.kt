package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.Wattage

class WattageValueBlockFactory(val color: Int) : SimpleValueBlockFactory<Wattage>(
    Wattage::class.java,
    0.0,
    100000.0,
    0.0,
    "W",
    "",
    null,
    CommonBlockCategories.Wattage
)