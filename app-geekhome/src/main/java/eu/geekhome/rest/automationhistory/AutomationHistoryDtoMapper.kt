package eu.geekhome.rest.automationhistory

import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.rest.automation.AutomationUnitDtoMapper
import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.repository.InstanceDto
import javax.inject.Inject

class AutomationHistoryDtoMapper @Inject constructor(
    private val automationUnitDtoMapper: AutomationUnitDtoMapper
) {
    @Throws(MappingException::class)
    fun map(timestamp: Long, unit: DeviceAutomationUnit<*>, instance: InstanceDto): AutomationHistoryDto {
        return AutomationHistoryDto(
            timestamp,
            automationUnitDtoMapper.map(unit, instance)
        )
    }
}

