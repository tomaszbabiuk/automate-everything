package eu.geekhome.coreplugin

import eu.geekhome.domain.automation.EvaluationResult
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    private val port: InputPort<T>
) :
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
