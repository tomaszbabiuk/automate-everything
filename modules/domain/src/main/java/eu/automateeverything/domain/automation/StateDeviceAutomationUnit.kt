package eu.automateeverything.domain.automation

interface StateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, actor: String? = null)
}