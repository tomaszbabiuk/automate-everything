package eu.automateeverything.data.automation

import eu.automateeverything.data.localization.Resource

data class EvaluationResultDto(
    val interfaceValue: Resource?,
    val isSignaled: Boolean,
    val descriptions: List<Resource>?,
    val error: String?,
    val nextStates: NextStatesDto?
)

