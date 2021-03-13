package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.Temperature
import java.util.*

class ThermometerAutomationUnit(private val port: Port<Temperature>) :
    IDeviceAutomationUnit<Temperature> {

    override fun calculate(now: Calendar) {
    }

    override fun buildEvaluationResult(): EvaluationResult {
        return EvaluationResult(value, value.toFormattedString(), false, null)
    }

    override val value: Temperature
        get() = port.read()

}
