package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.State

interface StateDeviceAutomationUnit : AutomationUnit<State> {
    @Throws(Exception::class)
    fun changeState(state: String, actor: String? = null)

    val currentState: State
}

