package eu.geekhome.rest.automationhistory

import eu.geekhome.rest.automation.AutomationUnitDto

data class AutomationHistoryDto(
    val timestamp: Long,
    val automationUnit: AutomationUnitDto
)

