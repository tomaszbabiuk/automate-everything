package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnit,
    override val next: IStatementNode?
) : StatementNodeBase() {

    override fun process(now: Calendar, firstLoop: Boolean) {
        deviceUnit.changeState(state)
        next?.process(now, firstLoop)
    }

    override fun modifyNote(noteId: String, note: Resource) {
        super.modifyNote(noteId, note)
        deviceUnit.updateNotes(notes.values.toList())
    }
}