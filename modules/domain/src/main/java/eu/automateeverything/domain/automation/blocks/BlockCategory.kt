package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.localization.Resource

interface BlockCategory {
    val categoryName: Resource
    val color: Int
}