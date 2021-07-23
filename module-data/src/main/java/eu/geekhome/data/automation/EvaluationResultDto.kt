package eu.geekhome.data.automation

import eu.geekhome.data.localization.Resource

data class EvaluationResultDto(
    val interfaceValue: Resource?,
    val controlMode: ControlMode,
    val isSignaled: Boolean,
    val descriptions: List<Resource>?,
    val error: String?,
    val nextStates: NextStatesDto?
)

