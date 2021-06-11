package eu.geekhome.rest.automation

import eu.geekhome.domain.repository.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

