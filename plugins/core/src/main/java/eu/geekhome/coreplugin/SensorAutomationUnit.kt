package eu.geekhome.coreplugin

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    private val port: Port<T>) :
    DeviceAutomationUnit<T>() {

    override val usedPortsIds: Array<String>
        get() = arrayOf(port.id)

    override fun calculateInternal(now: Calendar) {
        val value = port.read()
        lastEvaluation = buildEvaluationResult(value)
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    companion object {
        fun <T: PortValue> buildEvaluationResult(value: T) : EvaluationResult<T> {
            return EvaluationResult(
                interfaceValue = value.toFormattedString(),
                value = value,
            )
        }
    }

    override var lastEvaluation = buildEvaluationResult(port.read())
}
