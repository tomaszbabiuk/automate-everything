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
    private val controlPort: Port<Relay>
) : StateDeviceAutomationUnit(states, initialState) {
    @Throws(Exception::class)
    override fun calculate(now: Calendar) {
        if (currentStateId == "on") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(true))
        } else if (currentStateId == "off") {
            changeOutputPortStateIfNeeded<Any>(controlPort, Relay(false))
        }
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    init {
        setCurrentState("off")
    }
}