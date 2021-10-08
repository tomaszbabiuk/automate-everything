package eu.automateeverything.domain.automation

import eu.automateeverything.data.localization.Resource

interface StateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, code: String? = null, actor: String? = null)
    fun modifyNote(noteId: String, note: Resource)
}