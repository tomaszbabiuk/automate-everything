package eu.geekhome.rest.automation

import eu.geekhome.automation.UnitState
import eu.geekhome.services.repository.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val state: UnitState,
    val error: String,
    val evaluationResult: EvaluationResultDto
)

