package eu.automateeverything.onoffplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.hardware.OutputPort
import java.math.BigDecimal
import java.util.*

abstract class SinglePortRegulatorAutomationUnit<V: PortValue>(
    nameOfOrigin: String,
    private val instanceDto: InstanceDto,
    private val controlPort: OutputPort<V>,
    controlType: ControlType,
    private val stateChangeReporter: StateChangeReporter
) : AutomationUnitBase<V>(nameOfOrigin, controlType), ControllerAutomationUnit<V> {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        val actualLevel = controlPort.read()
        lastEvaluation = buildEvaluationResult(actualLevel)
        stateChangeReporter.reportDeviceValueChange(this, instanceDto)
    }

    override var lastEvaluation = buildEvaluationResult(controlPort.read())

    private fun buildEvaluationResult(level: V) : EvaluationResult<V> {
        return EvaluationResult(
            interfaceValue = level.toFormattedString(),
            value = level,
            isSignaled = level.asDecimal() > BigDecimal.ZERO,
            descriptions = lastNotes.values.toList()
        )
    }

    override fun control(newValue: V, actor: String?) {
        val actualLevel = controlPort.read()
        if (actualLevel.asDecimal() != newValue.asDecimal()) {
            controlPort.write(newValue)
            lastEvaluation = buildEvaluationResult(newValue)
            stateChangeReporter.reportDeviceValueChange(this, instanceDto)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}