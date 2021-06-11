package eu.geekhome.domain.automation

import eu.geekhome.domain.R
import java.util.*

abstract class DeviceAutomationUnit<T> {
    abstract var lastEvaluation: EvaluationResult<T>
    abstract val usedPortsIds: Array<String>
    abstract val recalculateOnTimeChange: Boolean
    abstract val recalculateOnPortUpdate: Boolean

    abstract fun calculateInternal(now: Calendar)

    fun calculate(now: Calendar) {
        return try {
            calculateInternal(now)
        } catch (ex: Exception) {
            lastEvaluation = evaluateAsAutomationError(ex)
        }
    }

    private fun evaluateAsAutomationError(ex: java.lang.Exception): EvaluationResult<T> {
        return EvaluationResult(
            interfaceValue = R.error_automation,
            error =  ex)
    }
}