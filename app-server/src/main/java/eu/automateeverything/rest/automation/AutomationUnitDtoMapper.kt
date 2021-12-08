package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.AutomationUnitDto
import eu.automateeverything.data.automation.ValueRangeDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import jakarta.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(unit: AutomationUnit<*>, instance: InstanceDto): AutomationUnitDto {
        return AutomationUnitDto(
            unit.controlType,
            extractValueRange(unit),
            instance,
            evaluationResultDtoMapper.map(unit.lastEvaluation)
        )
    }

    private fun extractValueRange(unit: AutomationUnit<*>): ValueRangeDto? {
        if (unit is ControllerAutomationUnit<*>) {
            return ValueRangeDto(unit.min, unit.max, unit.step)
        }

        return null
    }
}

