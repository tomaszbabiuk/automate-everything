package eu.geekhome.rest.automation

import eu.geekhome.data.automation.AutomationUnitDto
import eu.geekhome.data.instances.InstanceDto
import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.domain.automation.DeviceAutomationUnit
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

