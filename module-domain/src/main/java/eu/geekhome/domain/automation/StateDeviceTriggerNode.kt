package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto
import java.util.Calendar

class StateDeviceTriggerNode(
    stateChangeReporter: AutomationContext,
    private val instanceId: Long,
    private val unit: StateDeviceAutomationUnit,
    private val observedStateId: String,
    override val next: IStatementNode?
) : IStatementNode, StateChangedListener {

    init {
        stateChangeReporter.stateChangeReporter.addListener(this)
    }

    override fun process(now: Calendar) {
        //nothing to do here
    }

    override fun onChanged(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto) {
        if (instanceDto.id == instanceId) {
            if (deviceUnit.currentState.id == observedStateId) {
                next?.process(Calendar.getInstance())
            }
        }
    }
}