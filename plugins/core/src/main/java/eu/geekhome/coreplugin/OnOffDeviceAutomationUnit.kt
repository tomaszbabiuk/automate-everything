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
    private val _controlPort: Port<Relay>
) : StateDeviceAutomationUnit(states, initialState) {
    @Throws(Exception::class)
    override fun calculate(now: Calendar) {
        if (currentStateId == "on") {
            changeOutputPortStateIfNeeded<Any>(_controlPort, Relay(true))
        } else if (currentStateId == "off") {
            changeOutputPortStateIfNeeded<Any>(_controlPort, Relay(false))
        }
    }

    init {
        setCurrentState("off")
    }
}