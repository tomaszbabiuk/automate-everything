package eu.geekhome.rest.automation

import eu.geekhome.services.automation.UnitCondition
import eu.geekhome.services.repository.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val condition: UnitCondition,
    val error: String,
    val evaluationResult: EvaluationResultDto
)

