package eu.geekhome.rest.automation

import eu.geekhome.services.repository.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

