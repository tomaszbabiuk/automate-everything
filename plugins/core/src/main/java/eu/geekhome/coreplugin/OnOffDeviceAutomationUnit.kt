package eu.geekhome.coreplugin

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

    override fun buildNextStates(state: State): List<State> {
        if (state.id == "on") {
            return listOf(states["off"]!!)
        }

        return listOf(states["on"]!!)
    }

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (currentState.id == "on") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(true))
        } else if (currentState.id == "off") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(false))
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        if (controlPort.read().value) {
            changeState("on", eu.geekhome.data.automation.ControlMode.Manual, null, null)
        } else {
            changeState("off", eu.geekhome.data.automation.ControlMode.Manual, null, null)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}