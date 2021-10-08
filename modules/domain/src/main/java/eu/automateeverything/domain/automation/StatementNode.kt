package eu.automateeverything.domain.automation

import eu.automateeverything.data.localization.Resource
import java.util.*

interface StatementNode : AutomationNode {
    val next: StatementNode?
    fun process(now: Calendar, firstLoop: Boolean)
    fun modifyNote(noteId: String, note: Resource)
}