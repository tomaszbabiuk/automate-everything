package eu.geekhome.domain.automation

import eu.geekhome.data.automation.State

class StateDeviceAutomationUnit2(initialState: State?) {
    protected var state: State? = null
        protected set(value) {
            field = value
            changeState(state)
        }

    private fun changeState(state: State?) {
        //changing state
    }

    init {
        state = initialState
    }
}