package eu.geekhome.coreplugin

import eu.geekhome.coreplugin.OnOffDeviceConfigurable.Companion.STATE_OFF
import eu.geekhome.coreplugin.OnOffDeviceConfigurable.Companion.STATE_ON
import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class OnOffDeviceAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instanceDto: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val controlPort: OutputPort<Relay>,
) : StateDeviceAutomationUnit(stateChangeReporter, instanceDto, name, states, false) {

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
            changeState(STATE_ON, null, null)
        } else {
            changeState(STATE_OFF, null, null)
        }
    }

    init {
        calculateInternal(Calendar.getInstance())
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}