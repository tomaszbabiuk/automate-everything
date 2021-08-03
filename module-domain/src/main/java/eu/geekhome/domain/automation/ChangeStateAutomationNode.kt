package eu.geekhome.domain.automation

import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnit,
    override val next: IStatementNode?
) : IStatementNode {

    override fun process(now: Calendar, firstLoop: Boolean) {
        deviceUnit.changeState(state, null, "Blockly")
        next?.process(now, firstLoop)
    }
}