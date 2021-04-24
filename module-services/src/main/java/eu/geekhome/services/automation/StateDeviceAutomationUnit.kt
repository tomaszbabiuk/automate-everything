package eu.geekhome.services.automation

import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.Relay

abstract class StateDeviceAutomationUnit(
    private val states: Map<String, State>,
    initialState: String) :
    DeviceAutomationUnit<State>(buildEvaluationResult(states[initialState]!!, ControlMode.Auto)), IStateDeviceAutomationUnit {

    var currentState: State
    override var controlMode: ControlMode = ControlMode.Auto

    protected fun setCurrentState(stateId: String) {
        this.currentState = states[stateId]!!
    }

    override fun changeState(state: String, controlMode: ControlMode, code: String?, actor: String?) {
        if (currentState.id != state || controlMode != controlMode) {
            setCurrentState(state)
            this.controlMode = controlMode
            configureNewState(state)

            lastEvaluation = buildEvaluationResult(currentState, controlMode)
        }
    }

    abstract fun configureNewState(state: String)

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

        fun buildEvaluationResult(state: State, controlMode: ControlMode) : EvaluationResult<State> {
            return EvaluationResult(
                interfaceValue = state.name,
                value = state,
                isSignaled = state.isSignaled,
                controlMode = controlMode
            )
        }
    }

    init {
        currentState = states[initialState]!!
    }
}