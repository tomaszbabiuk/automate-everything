package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.R
import java.util.*

abstract class DeviceAutomationUnit<T>(var nameOfOrigin: String?) {
    abstract var lastEvaluation: EvaluationResult<T>
    abstract val usedPortsIds: Array<String>
    abstract val recalculateOnTimeChange: Boolean
    abstract val recalculateOnPortUpdate: Boolean

    abstract fun calculateInternal(now: Calendar)

    fun calculate(now: Calendar) {
        return try {
            calculateInternal(now)
        } catch (ex: Exception) {
            val aex = AutomationErrorException(R.error_automation, ex)
            lastEvaluation = evaluateAsAutomationError(aex)
        }
    }

    fun markExternalError(ex: AutomationErrorException) {
        lastEvaluation = evaluateAsAutomationError(ex)
    }

    private fun evaluateAsAutomationError(ex: AutomationErrorException): EvaluationResult<T> {
        return EvaluationResult(
            interfaceValue = R.error_automation,
            error =  ex,
            descriptions  = listOf(ex.localizedMessage))
    }

    open fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, DeviceAutomationUnit<*>>>) {
    }
}