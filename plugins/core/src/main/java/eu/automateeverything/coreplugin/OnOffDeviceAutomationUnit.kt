package eu.automateeverything.coreplugin

import eu.automateeverything.coreplugin.OnOffDeviceConfigurable.Companion.STATE_OFF
import eu.automateeverything.coreplugin.OnOffDeviceConfigurable.Companion.STATE_ON
import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class OnOffDeviceAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val controlPort: OutputPort<Relay>,
    private val readOnly: Boolean
) : StateDeviceAutomationUnitBase(stateChangeReporter, instanceDto, name, states, false) {

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == STATE_ON) {
            changeRelayStateIfNeeded(controlPort, Relay(true))
        } else if (currentState.id == STATE_OFF) {
            changeRelayStateIfNeeded(controlPort, Relay(false))
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        if (controlPort.read().value) {
            changeState(STATE_ON, null)
        } else {
            changeState(STATE_OFF, null)
        }
    }

    init {
        calculateInternal(Calendar.getInstance())
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override fun buildNextStates(state: State): NextStatesDto {
        if (readOnly) {
            return NextStatesDto(listOf(), state.id, requiresExtendedWidth)
        }

        return super.buildNextStates(state)
    }
}