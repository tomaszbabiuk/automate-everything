package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnit,
    override val next: IStatementNode?
) : IStatementNode {

    override fun process(now: Calendar, firstLoop: Boolean, notes: MutableList<Resource>) {
        deviceUnit.changeState(state, null, "Blockly")
        deviceUnit.updateNotes(notes)
        next?.process(now, firstLoop, notes)
    }
}