package eu.automateeverything.data.automationhistory

import eu.automateeverything.data.localization.Resource

data class AutomationHistoryDto(
    val no: Int,
    val timestamp: Long,
    val subject: Resource,
    val change: Resource,
    val iconId: Long?,
)

