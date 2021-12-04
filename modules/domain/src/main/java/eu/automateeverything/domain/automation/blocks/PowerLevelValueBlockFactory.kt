package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.domain.hardware.PowerLevel

class PowerLevelValueBlockFactory(val color: Int) : SimpleValueBlockFactory<PowerLevel>(
    PowerLevel::class.java,
    0.0,
    100.0,
    0.0,
    "%",
    "",
    null,
    CommonBlockCategories.PowerLevel
)