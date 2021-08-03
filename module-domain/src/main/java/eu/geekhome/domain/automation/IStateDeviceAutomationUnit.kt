package eu.geekhome.domain.automation

interface IStateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, code: String? = null, actor: String? = null)
}