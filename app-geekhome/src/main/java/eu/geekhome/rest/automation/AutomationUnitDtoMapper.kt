package eu.geekhome.rest.automation

import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.repository.InstanceDto
import javax.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(unit: IDeviceAutomationUnit<*>, instance: InstanceDto): AutomationUnitDto {
        return AutomationUnitDto(
            instance,
            if (unit.lastEvaluation != null) {
                evaluationResultDtoMapper.map(unit.lastEvaluation!!)
            } else {
                null
            }
        )
    }
}

