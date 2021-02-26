package eu.geekhome.automation

import eu.geekhome.services.automation.ControlMode
import eu.geekhome.services.automation.StateDeviceAutomationUnit
import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnit,
    override val next: StatementNode?,
) : StatementNode {

    override fun process(now: Calendar) {
        deviceUnit.changeState(state, ControlMode.Manual, null, "Blockly")
        deviceUnit.calculate(now)

        next?.process(now)
    }
}