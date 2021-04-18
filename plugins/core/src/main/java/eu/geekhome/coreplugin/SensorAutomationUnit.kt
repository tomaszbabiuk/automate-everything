package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    valueType: Class<T>,
    private val port: Port<T>) :
    IDeviceAutomationUnit<T>(valueType) {

    override fun calculate(now: Calendar) {
    }

    override fun buildEvaluationResultInternally(): EvaluationResult {
        return EvaluationResult(value, value.toFormattedString(), false, null)
    }

    override val value: T
        get() = port.read()

    override val usedPortsIds: Array<String>
        get() = arrayOf(port.id)
}
