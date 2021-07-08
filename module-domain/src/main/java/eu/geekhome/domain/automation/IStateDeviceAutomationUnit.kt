package eu.geekhome.domain.automation

import eu.geekhome.data.automation.ControlMode

interface IStateDeviceAutomationUnit {

    @Throws(Exception::class)
    fun changeState(state: String, controlMode: ControlMode, code: String? = null, actor: String? = null)

    var controlMode: ControlMode
}