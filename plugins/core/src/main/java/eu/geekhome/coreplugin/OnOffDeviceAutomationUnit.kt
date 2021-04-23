package eu.geekhome.coreplugin

import eu.geekhome.services.automation.ControlMode
import eu.geekhome.services.automation.State
import eu.geekhome.services.automation.StateDeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.Relay
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class OnOffDeviceAutomationUnit(
    states: Map<String, State>,
    initialState: String,
    private val controlPort: Port<Relay>,
) : StateDeviceAutomationUnit(states, initialState) {

    @Throws(Exception::class)
    override fun configureNewState(state: String) {
        if (currentState.name.id == "on") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(true))
        } else if (currentState.name.id == "off") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(false))
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    init {
        setCurrentState("off")
    }

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