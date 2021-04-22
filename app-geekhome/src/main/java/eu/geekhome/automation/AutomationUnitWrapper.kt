package eu.geekhome.automation

import eu.geekhome.services.R
import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import java.util.*

class AutomationUnitWrapper<T>(
    valueType: Class<T>
) : IDeviceAutomationUnit<T>() {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    fun setupForInitError(ex: Exception) {
        lastEvaluation = EvaluationResult(
            interfaceValue = R.error_initialization,
            error = ex
        )
    }

    override fun calculateInternal(now: Calendar): Boolean {
        return false
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
}