package eu.geekhome.automation

import eu.geekhome.domain.R
import eu.geekhome.domain.automation.AutomationErrorException
import eu.geekhome.domain.automation.EvaluationResult
import eu.geekhome.domain.automation.DeviceAutomationUnit
import java.util.*

class AutomationUnitWrapper<T>(
    name: String?,
    valueType: Class<T>,
    initError: AutomationErrorException
) : DeviceAutomationUnit<T>(name) {

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
}