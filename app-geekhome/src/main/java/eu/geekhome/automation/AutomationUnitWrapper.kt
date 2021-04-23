package eu.geekhome.automation

import eu.geekhome.services.R
import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.DeviceAutomationUnit
import java.util.*

class AutomationUnitWrapper<T>(
    valueType: Class<T>,
    initError: Exception
) : DeviceAutomationUnit<T>(buildErrorEvaluation(initError)) {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

    companion object {
        fun <T> buildErrorEvaluation(ex: Exception) : EvaluationResult<T> {
            return EvaluationResult(
                interfaceValue = R.error_initialization,
                error = ex
            )
        }
    }
}