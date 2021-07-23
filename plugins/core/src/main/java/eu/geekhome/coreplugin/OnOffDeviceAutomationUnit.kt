package eu.geekhome.coreplugin

import eu.geekhome.data.automation.ControlMode
import eu.geekhome.data.automation.State
import eu.geekhome.domain.automation.StateDeviceAutomationUnit
import eu.geekhome.domain.hardware.OutputPort
import eu.geekhome.domain.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class OnOffDeviceAutomationUnit(
    name: String?,
    states: Map<String, State>,
    initialState: String,
    private val controlPort: OutputPort<Relay>,
) : StateDeviceAutomationUnit(name, states, initialState) {

    init {
        setCurrentState("off")
    }

    override val requiresExtendedWidth = false

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == "on") {
            changeRelayStateIfNeeded(controlPort, Relay(true))
        } else if (currentState.id == "off") {
            changeRelayStateIfNeeded(controlPort, Relay(false))
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        if (controlPort.read().value) {
            changeState("on", ControlMode.Manual, null, null)
        } else {
            changeState("off", ControlMode.Manual, null, null)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}