package eu.geekhome.rest.automationhistory

import eu.geekhome.domain.localization.Resource

data class AutomationHistoryDto(
    val no: Int,
    val timestamp: Long,
    val subject: Resource,
    val change: Resource,
    val iconId: Long?,
)

