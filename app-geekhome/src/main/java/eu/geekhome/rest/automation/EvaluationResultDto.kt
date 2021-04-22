package eu.geekhome.rest.automation

import eu.geekhome.services.automation.ControlMode
import eu.geekhome.services.localization.Resource

data class EvaluationResultDto(
    val interfaceValue: Resource?,
    val controlMode: ControlMode,
    val isSignaled: Boolean,
    val descriptions: Map<String, Resource>?,
    val error: String?
)