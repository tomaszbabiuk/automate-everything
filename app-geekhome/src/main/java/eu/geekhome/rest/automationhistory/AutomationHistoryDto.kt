package eu.geekhome.rest.automationhistory

import eu.geekhome.services.localization.Resource

data class AutomationHistoryDto(
    val timestamp: Long,
    val subject: Resource,
    val change: Resource,
    val no: Int
)

