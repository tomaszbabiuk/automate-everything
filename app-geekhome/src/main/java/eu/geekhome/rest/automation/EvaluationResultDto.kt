package eu.geekhome.rest.automation

import eu.geekhome.domain.automation.ControlMode
import eu.geekhome.domain.automation.State
import eu.geekhome.domain.localization.Resource

data class EvaluationResultDto(
    val interfaceValue: Resource?,
    val controlMode: ControlMode,
    val isSignaled: Boolean,
    val descriptions: List<Resource>?,
    val error: String?,
    val nextStates: List<State>?
)