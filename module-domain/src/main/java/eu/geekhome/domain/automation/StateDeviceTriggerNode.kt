package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto
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

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (firstLoop && unit.currentState.id == observedStateId) {
            next?.process(now, firstLoop)
        }
    }

    override fun onChanged(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
        if (instanceDto.id == instanceId) {
            if (deviceUnit.currentState.id == observedStateId) {
                next?.process(Calendar.getInstance(), false)
            }
        }
    }
}