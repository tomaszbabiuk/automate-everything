package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import java.util.Calendar

class StateDeviceTriggerNode(
    context: AutomationContext,
    private val instanceId: Long,
    private val unit: StateDeviceAutomationUnit,
    private val observedStateId: String,
    override val next: IStatementNode?
) : IStatementNode, StateChangedListener {

    init {
        context.stateChangeReporter.addListener(this)
    }

    private var notesFromProcessing = mutableListOf<Resource>()

    override fun process(now: Calendar, firstLoop: Boolean, notes: MutableList<Resource>) {
        notesFromProcessing = notes
        if (firstLoop && unit.currentState.id == observedStateId) {
            next?.process(now, firstLoop, notes)
        }
    }

    override fun onChanged(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
        if (instanceDto.id == instanceId) {
            if (deviceUnit.currentState.id == observedStateId) {
                next?.process(Calendar.getInstance(), false, notesFromProcessing)
            }
        }
    }
}