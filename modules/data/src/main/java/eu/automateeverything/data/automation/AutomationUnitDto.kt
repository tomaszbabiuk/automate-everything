package eu.automateeverything.data.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto

data class AutomationUnitDto(
    val type: ControlType,
    val valueRange: ValueRangeDto? = null, //present when type == ControlType.Controller
    val instance: InstanceDto,
    val evaluationResult: EvaluationResultDto?
)

