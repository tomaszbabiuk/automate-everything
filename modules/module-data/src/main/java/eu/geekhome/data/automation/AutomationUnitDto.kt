package eu.geekhome.data.automation

import eu.geekhome.data.instances.InstanceDto

data class AutomationUnitDto(
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

