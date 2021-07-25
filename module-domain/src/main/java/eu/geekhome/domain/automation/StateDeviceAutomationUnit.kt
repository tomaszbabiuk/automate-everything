package eu.geekhome.domain.automation

import eu.geekhome.data.automation.ControlMode
import eu.geekhome.data.automation.NextStatesDto
import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.PowerLevel
import eu.geekhome.domain.hardware.Relay

abstract class StateDeviceAutomationUnit(
    private val stateChangeReporter: StateChangeReporter,
    private val instanceDto: InstanceDto,
    name: String,
    private val states: Map<String, State>,
    initialStateId: String,
    private val requiresExtendedWidth: Boolean) :
    DeviceAutomationUnit<State>(name), IStateDeviceAutomationUnit {

    var currentState: State = states[initialStateId]!!
        protected set(value) {
            field = value
            changeState(value.id, ControlMode.Manual, null, null)
        }

    override var controlMode: ControlMode = ControlMode.Auto

    override fun changeState(state: String, controlMode: ControlMode, code: String?, actor: String?) {
        if (currentState.id != state || controlMode != controlMode) {
            currentState = states[state]!!
            this@StateDeviceAutomationUnit.controlMode = controlMode
            applyNewState(state)

            lastEvaluation = buildEvaluationResult(currentState.id, states, controlMode)
            stateChangeReporter.reportDeviceStateChange(this, instanceDto)
        }
    }

    private fun buildEvaluationResult(initialStateId: String, states: Map<String, State>, controlMode: ControlMode) : EvaluationResult<State> {
        val state = states[initialStateId]!!
        return EvaluationResult(
            interfaceValue = state.name,
            value = state,
            isSignaled = state.isSignaled,
            controlMode = controlMode,
            nextStates = buildNextStates(state)
        )
    }

    open fun buildNextStates(state: State): NextStatesDto {
        val nextStates = states
            .filter { it.value.type != StateType.ReadOnly }
            .map { it.value }
        return NextStatesDto(nextStates, state.id, requiresExtendedWidth)
    }

    abstract fun applyNewState(state: String)

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        protected fun changeRelayStateIfNeeded(
            port: OutputPort<Relay>,
            state: Relay,
            invalidate: Boolean = false
        ) {
            if (invalidate || state.value != port.read().value) {
                port.write(state)
            }
        }

        @Throws(Exception::class)
        @JvmStatic
        protected fun changePowerLevelIfNeeded(
            port: OutputPort<PowerLevel>,
            state: PowerLevel,
            invalidate: Boolean = false
        ) {
            if (invalidate || state.value != port.read().value) {
                port.write(state)
            }
        }
    }

    override var lastEvaluation = buildEvaluationResult(initialStateId, states, ControlMode.Auto)
}