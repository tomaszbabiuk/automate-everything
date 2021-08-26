package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.PortValue

interface BlockCategory {
    val categoryName: Resource
    val color: Int
}