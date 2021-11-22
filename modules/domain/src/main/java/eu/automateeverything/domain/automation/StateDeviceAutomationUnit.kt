package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.State

abstract class StateDeviceAutomationUnit(nameOfOrigin: String) : AutomationUnit<State>(nameOfOrigin) {
    @Throws(Exception::class)
    abstract fun changeState(state: String, actor: String? = null)
}

