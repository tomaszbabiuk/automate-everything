package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import java.util.*

abstract class AutomationUnit<T>(var nameOfOrigin: String?) {
    abstract var lastEvaluation: EvaluationResult<T>
    abstract val usedPortsIds: Array<String>
    abstract val recalculateOnTimeChange: Boolean
    abstract val recalculateOnPortUpdate: Boolean
    abstract val controlType: ControlType

    abstract fun calculateInternal(now: Calendar)

    fun modifyNote(noteId: String, note: Resource) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes[noteId] = note
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
            evaluateAndReportStateUpdate()
        }
    }

    protected var lastNotes: MutableMap<String, Resource> = HashMap()

    fun removeNote(noteId: String) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes.remove(noteId)
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
            evaluateAndReportStateUpdate()
        }
    }

    private fun evaluateAndReportStateUpdate() {
//        lastEvaluation = buildEvaluationResult(currentState.id, states)
//        stateChangeReporter.reportDeviceStateUpdated(this, instanceDto)
    }

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

    open fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
    }
}