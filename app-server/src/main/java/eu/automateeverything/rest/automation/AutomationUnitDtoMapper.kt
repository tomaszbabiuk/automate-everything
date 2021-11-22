package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.AutomationUnitDto
import eu.automateeverything.data.instances.InstanceDto
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.AutomationUnit
import jakarta.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(unit: AutomationUnit<*>, instance: InstanceDto): AutomationUnitDto {
        return AutomationUnitDto(
            unit.controlType,
            instance,
            evaluationResultDtoMapper.map(unit.lastEvaluation)
        )
    }
}

