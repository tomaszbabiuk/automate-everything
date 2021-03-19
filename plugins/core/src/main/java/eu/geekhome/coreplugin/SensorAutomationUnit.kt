package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    override val valueType: Class<T>,
    private val port: Port<T>, ) :
    IDeviceAutomationUnit<T> {

    override fun calculate(now: Calendar) {
    }

    override fun buildEvaluationResult(): EvaluationResult {
        return EvaluationResult(value, value.toFormattedString(), false, null)
    }

    override val value: T
        get() = port.read()

}
