package eu.automateeverything.domain.automation

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.util.*

class ChangeValueAutomationNode(
    private val value: ValueNode?,
    private val deviceUnit: ControllerAutomationUnit<in PortValue>,
    override val next: StatementNode?
) : StatementNodeBase() {

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (value != null) {
            val controlValue = value.getValue(now)
            if (controlValue != null) {
                deviceUnit.control(controlValue)
            }
        }

        next?.process(now, firstLoop)
    }

    override fun modifyNote(noteId: String, note: Resource) {
        super.modifyNote(noteId, note)
        deviceUnit.modifyNote(noteId, note)
    }
}