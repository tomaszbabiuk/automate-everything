package eu.geekhome.coreplugin

import eu.geekhome.coreplugin.OnOffDeviceConfigurable.Companion.STATE_OFF
import eu.geekhome.coreplugin.OnOffDeviceConfigurable.Companion.STATE_ON
import eu.geekhome.data.automation.ControlMode
import eu.geekhome.data.automation.State
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class OnOffDeviceAutomationUnit(
    name: String,
    states: Map<String, State>,
    initialStateId: String,
    private val controlPort: OutputPort<Relay>,
) : StateDeviceAutomationUnit(name, states, initialStateId, false) {

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
            changeState(STATE_ON, ControlMode.Manual, null, null)
        } else {
            changeState(STATE_OFF, ControlMode.Manual, null, null)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}