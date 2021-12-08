package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import java.util.*

interface AutomationUnit<T> {
    val nameOfOrigin: String
    var lastEvaluation: EvaluationResult<T>
    val usedPortsIds: Array<String>
    val recalculateOnTimeChange: Boolean
    val recalculateOnPortUpdate: Boolean
    val controlType: ControlType
    fun calculate(now: Calendar)
    fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>)
    fun markExternalError(ex: AutomationErrorException)
    fun modifyNote(noteId: String, note: Resource)
    fun removeNote(noteId: String)
}