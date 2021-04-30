package eu.geekhome.rest.automation

import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.repository.InstanceDto
import javax.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(unit: DeviceAutomationUnit<*>, instance: InstanceDto): AutomationUnitDto {
        return AutomationUnitDto(
            instance,
            evaluationResultDtoMapper.map(unit.lastEvaluation)
        )
    }
}

