package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.domain.R
import java.util.*

class AutomationUnitWrapper<T>(
    name: String?,
    valueType: Class<T>,
    initError: AutomationErrorException
) : AutomationUnit<T>(name) {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

    override var lastEvaluation = EvaluationResult<T>(
        interfaceValue = R.error_initialization,
        error = initError,
        descriptions = listOf(initError.localizedMessage)
    )
    override val controlType = ControlType.NA
}