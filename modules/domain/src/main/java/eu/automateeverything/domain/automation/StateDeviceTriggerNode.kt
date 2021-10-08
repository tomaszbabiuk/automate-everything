package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto
import java.util.*

class StateDeviceTriggerNode(
    context: AutomationContext,
    private val instanceId: Long,
    private val unit: StateDeviceAutomationUnitBase,
    private val observedStateId: String,
    override val next: StatementNode?
) : StatementNodeBase(), StateChangedListener {

    init {
        context.stateChangeReporter.addListener(this)
    }

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (firstLoop && unit.currentState.id == observedStateId) {
            next?.process(now, firstLoop)
        }
    }

    override fun onChanged(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto) {
        if (instanceDto.id == instanceId) {
            if (deviceUnit.currentState.id == observedStateId) {
                next?.process(Calendar.getInstance(), false)
            }
        }
    }
}