package eu.automateeverything.rest.automationhistory

import eu.automateeverything.R
import eu.automateeverything.data.automationhistory.AutomationHistoryDto
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.events.AutomationStateEventData
import eu.automateeverything.domain.events.AutomationUpdateEventData
import eu.automateeverything.data.localization.Resource
import jakarta.inject.Inject

class AutomationHistoryDtoMapper @Inject constructor() {

    @Throws(MappingException::class)
    fun map(timestamp: Long, event: AutomationUpdateEventData, id: Int): AutomationHistoryDto {
        return AutomationHistoryDto(
            id,
            timestamp,
            Resource.createUniResource(event.instance.fields["name"]!!),
            event.evaluation.interfaceValue,
            event.instance.iconId
        )
    }

    fun map(timestamp: Long, event: AutomationStateEventData, id: Int): AutomationHistoryDto {
        return AutomationHistoryDto(
            id,
            timestamp,
            R.automation_history_automation,
            if (event.enabled) R.automation_history_enabled else R.automation_history_disabled,
            null
        )
    }
}

