package eu.automateeverything.coreplugin

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.ControllerAutomationUnitBase
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.hardware.OutputPort
import java.math.BigDecimal
import java.util.*

abstract class SinglePortRegulatorAutomationUnit<V: PortValue>(
    nameOfOrigin: String,
    private val instanceDto: InstanceDto,
    private val controlPort: OutputPort<V>,
    automationOnly: Boolean,
    private val stateChangeReporter: StateChangeReporter
) : ControllerAutomationUnitBase<V>(nameOfOrigin, automationOnly) {

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
}