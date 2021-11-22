package eu.automateeverything.data.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto

data class AutomationUnitDto(
    val type: ControlType,
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

