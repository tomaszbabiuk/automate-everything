package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource

interface IStateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, code: String? = null, actor: String? = null)
    fun updateNotes(notes: List<Resource> = listOf())
}