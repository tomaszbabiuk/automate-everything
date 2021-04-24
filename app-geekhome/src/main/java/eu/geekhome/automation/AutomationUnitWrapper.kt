package eu.geekhome.automation

import eu.geekhome.services.R
import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.DeviceAutomationUnit
import java.util.*

class AutomationUnitWrapper<T>(
    valueType: Class<T>,
    initError: Exception
) : DeviceAutomationUnit<T>() {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

    override var lastEvaluation = EvaluationResult<T>(
        interfaceValue = R.error_initialization,
        error = initError
    )
}