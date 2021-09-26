package eu.automateeverything.data.automation

import eu.automateeverything.data.instances.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

