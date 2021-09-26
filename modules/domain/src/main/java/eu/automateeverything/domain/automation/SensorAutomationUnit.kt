package eu.automateeverything.domain.automation

import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    name: String?,
    private val port: InputPort<T>
) :
    DeviceAutomationUnit<T>(name) {

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
