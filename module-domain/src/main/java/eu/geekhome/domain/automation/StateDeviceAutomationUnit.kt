package eu.geekhome.domain.automation

import eu.geekhome.data.automation.NextStatesDto
import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.PowerLevel
import eu.geekhome.domain.hardware.Relay

abstract class StateDeviceAutomationUnit(
    private val stateChangeReporter: StateChangeReporter,
    private val instanceDto: InstanceDto,
    name: String,
    private val states: Map<String, State>,
    private val requiresExtendedWidth: Boolean) :
    DeviceAutomationUnit<State>(name), IStateDeviceAutomationUnit {

    private var lastNotes: List<Resource>? = null

    var currentState: State = states[STATE_UNKNOWN]!!
        protected set(value) {
            field = value
            changeState(value.id, null, null)
        }

    override fun changeState(state: String, code: String?, actor: String?) {
        if (currentState.id != state) {
            currentState = states[state]!!
            applyNewState(state)
            evaluateAndReportStateChange()
        }
    }

    override fun updateNotes(notes: List<Resource>) {
        if (notes != lastNotes) {
            lastNotes = notes
            evaluateAndReportStateUpdate()
        }
    }

    private fun evaluateAndReportStateChange() {
        lastEvaluation = buildEvaluationResult(currentState.id, states)
        stateChangeReporter.reportDeviceStateChange(this, instanceDto)
    }

    private fun evaluateAndReportStateUpdate() {
        lastEvaluation = buildEvaluationResult(currentState.id, states)
        stateChangeReporter.reportDeviceStateUpdated(this, instanceDto)
    }

    private fun buildEvaluationResult(initialStateId: String, states: Map<String, State>) : EvaluationResult<State> {
        val state = states[initialStateId]!!
        return EvaluationResult(
            interfaceValue = state.name,
            value = state,
            isSignaled = state.isSignaled,
            nextStates = buildNextStates(state),
            descriptions = if (lastNotes != null) {
                lastNotes!!
            } else {
                listOf()
            }
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

    override var lastEvaluation = buildEvaluationResult(STATE_UNKNOWN, states)
}