package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.automation.StateType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import eu.automateeverything.domain.hardware.Relay

abstract class StateDeviceAutomationUnit(
    private val stateChangeReporter: StateChangeReporter,
    private val instanceDto: InstanceDto,
    name: String,
    protected val states: Map<String, State>,
    private val requiresExtendedWidth: Boolean) :
    DeviceAutomationUnit<State>(name), IStateDeviceAutomationUnit {

    private var lastNotes: MutableMap<String, Resource> = HashMap()

    var currentState: State = states[STATE_UNKNOWN]!!
        protected set(value) {
            field = value
            changeState(value.id, null, null)
        }

    protected fun statesExcept(currentState: State, excludedStates: Array<String>): NextStatesDto {
        val nextStates = states
            .map { it.value }
            .filter { it.type != StateType.ReadOnly }
            .filter { it.id !in excludedStates }
        return NextStatesDto(nextStates, currentState.id, requiresExtendedWidth)
    }

    protected fun onlyOneState(stateId: String): NextStatesDto {
        val nextStates = listOf(states[stateId]!!)
        return NextStatesDto(nextStates, currentState.id, requiresExtendedWidth)
    }

    override fun changeState(state: String, code: String?, actor: String?) {
        if (currentState.id != state) {
            currentState = states[state]!!
            applyNewState(state)
            evaluateAndReportStateChange()
        }
    }

    override fun modifyNote(noteId: String, note: Resource) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes[noteId] = note
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
            evaluateAndReportStateUpdate()
        }
    }

    fun removeNote(noteId: String) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes.remove(noteId)
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
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
            descriptions = lastNotes.values.toList()
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