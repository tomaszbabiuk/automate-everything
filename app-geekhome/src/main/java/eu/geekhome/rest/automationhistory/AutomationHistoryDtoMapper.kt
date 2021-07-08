package eu.geekhome.rest.automationhistory

import eu.geekhome.R
import eu.geekhome.data.automationhistory.AutomationHistoryDto
import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.domain.events.AutomationStateEventData
import eu.geekhome.domain.events.AutomationUpdateEventData
import eu.geekhome.data.localization.Resource
import javax.inject.Inject

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

