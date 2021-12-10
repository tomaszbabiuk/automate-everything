package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class SensorAutomationUnit<T: PortValue>(
    name: String,
    private val port: InputPort<T>
) :
    AutomationUnitBase<T>(name, ControlType.NA) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(port.id)

    override fun calculateInternal(now: Calendar) {
        val value = port.read()
        lastEvaluation = buildEvaluationResult(value)
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override var lastEvaluation = buildEvaluationResult(port.read())
}
