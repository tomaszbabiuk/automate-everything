package eu.geekhome.domain.automation

import eu.geekhome.data.automation.ControlMode
import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnit,
    override val next: IStatementNode?,
    private val context: AutomationContext
) : IStatementNode {

    override fun process(now: Calendar) {
        val previousState = deviceUnit.currentState
        deviceUnit.changeState(state, ControlMode.Manual, null, "Blockly")

        val newState = deviceUnit.currentState
        if (previousState.name != newState.name) {
            context.reportDeviceStateChange(deviceUnit)
        }

        next?.process(now)
    }
}