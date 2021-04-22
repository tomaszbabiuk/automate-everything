package eu.geekhome.coreplugin

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

    override fun calculateInternal(now: Calendar): Boolean {
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)

    override fun calculateNewState(now: Calendar) {
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

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
}