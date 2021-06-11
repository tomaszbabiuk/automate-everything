package eu.geekhome.automation.blocks

import eu.geekhome.R
import eu.geekhome.domain.hardware.Wattage

class WattageValueBlockFactory(val color: Int) : SimpleValueBlockFactory<Wattage>(
    Wattage::class.java,
    0.0,
    100000.0,
    0.0,
    "W",
    "",
    null,
    R.category_wattage,
    color
)