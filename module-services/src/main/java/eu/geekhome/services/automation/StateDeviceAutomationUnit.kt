package eu.geekhome.services.automation

import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.Relay
import java.util.*

abstract class StateDeviceAutomationUnit(
    private val states: Map<String, State>,
    initialState: String) :

    IDeviceAutomationUnit<State>(null), IStateDeviceAutomationUnit {

    var currentState: State
    override var controlMode: ControlMode = ControlMode.Auto

    protected fun setCurrentState(currentState: String) {
        this.currentState = states[currentState]!!
    }

    override fun changeState(state: String, controlMode: ControlMode, code: String?, actor: String?) {
        if (currentState.name.id != state || controlMode != controlMode) {
            setCurrentState(state)
            this.controlMode = controlMode
        }
    }

    @Throws(Exception::class)
    fun changeStateInternal(state: String, controlMode: ControlMode) {
        changeState(state, controlMode, null, "SYSTEM")
    }

    protected abstract fun calculateNewState(now: Calendar)

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        protected fun <T> changeOutputPortStateIfNeeded(
            port: Port<Relay>?,
            state: Relay?,
            invalidate: Boolean = false
        ) {
            if (port != null && state != null && (invalidate || !state.equals(port.read()))) {
                port.write(state)
            }
        }
    }

    init {
        currentState = states[initialState]!!
    }
}