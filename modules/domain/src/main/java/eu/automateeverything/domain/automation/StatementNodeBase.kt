package eu.automateeverything.domain.automation

import eu.automateeverything.data.localization.Resource

abstract class StatementNodeBase : IStatementNode {
    protected val notes: MutableMap<String, Resource> = HashMap()

    override fun modifyNote(noteId: String, note: Resource) {
        notes[noteId] = note
    }
}