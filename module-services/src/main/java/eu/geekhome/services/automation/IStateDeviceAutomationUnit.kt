package eu.geekhome.services.automation

interface IStateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, controlMode: ControlMode, code: String?, actor: String?)

    var controlMode: ControlMode
}