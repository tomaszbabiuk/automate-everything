package eu.geekhome.domain.automation

import java.util.Calendar

class StateDeviceTriggerNode(
    private val unit: StateDeviceAutomationUnit,
    private val observedStateId: String,
    override val next: IStatementNode?
    ) : IStatementNode {

    override fun process(now: Calendar) {
        if (unit.currentState.id == observedStateId) {
            next?.process(now)
        }
    }
}