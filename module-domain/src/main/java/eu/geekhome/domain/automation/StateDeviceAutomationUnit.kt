package eu.geekhome.domain.automation

import eu.geekhome.data.automation.ControlMode
import eu.geekhome.data.automation.State
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.Relay

abstract class StateDeviceAutomationUnit(
    name: String?,
    protected val states: Map<String, State>,
    initialStateId: String) :
    DeviceAutomationUnit<State>(name), IStateDeviceAutomationUnit {

    var currentState: State
    override var controlMode: ControlMode = ControlMode.Auto

    protected fun setCurrentState(stateId: String) {
        this.currentState = states[stateId]!!
    }

    override fun changeState(state: String, controlMode: ControlMode, code: String?, actor: String?) {
        if (currentState.id != state || controlMode != controlMode) {
            setCurrentState(state)
            this.controlMode = controlMode
            applyNewState(state)

            lastEvaluation = buildEvaluationResult(currentState.id, states, controlMode)
        }
    }

    fun buildEvaluationResult(initialStateId: String, states: Map<String, State>, controlMode: ControlMode) : EvaluationResult<State> {
        val state = states[initialStateId]!!
        return EvaluationResult(
            interfaceValue = state.name,
            value = state,
            isSignaled = state.isSignaled,
            controlMode = controlMode,
            nextStates = buildNextStates(state)
        )
    }

    abstract fun buildNextStates(state: State): List<State>

    abstract fun applyNewState(state: String)

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        protected fun <T> changeOutputPortStateIfNeeded(
            port: OutputPort<Relay>,
            state: Relay?,
            invalidate: Boolean = false
        ) {
            if (state != null &&
                (invalidate || state != port.read())) {
                port.write(state)
            }
        }
    }

    override var lastEvaluation = buildEvaluationResult(initialStateId, states, ControlMode.Auto)

    init {
        currentState = states[initialStateId]!!
    }
}