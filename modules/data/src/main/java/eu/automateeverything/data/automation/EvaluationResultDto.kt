package eu.automateeverything.data.automation

import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

data class EvaluationResultDto(
    val interfaceValue: Resource,
    val decimalValue: BigDecimal?,
    val isSignaled: Boolean,
    val descriptions: List<Resource>?,
    val error: String?,
    val nextStates: NextStatesDto?,
)

