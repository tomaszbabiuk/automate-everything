package eu.automateeverything.domain.automation

import eu.automateeverything.data.localization.Resource
import java.util.*

class ChangeStateAutomationNode(
    private val state: String,
    private val deviceUnit: StateDeviceAutomationUnitBase,
    override val next: StatementNode?
) : StatementNodeBase() {

    override fun process(now: Calendar, firstLoop: Boolean) {
        deviceUnit.changeState(state)
        next?.process(now, firstLoop)
    }

    override fun modifyNote(noteId: String, note: Resource) {
        super.modifyNote(noteId, note)
        deviceUnit.modifyNote(noteId, note)
    }
}