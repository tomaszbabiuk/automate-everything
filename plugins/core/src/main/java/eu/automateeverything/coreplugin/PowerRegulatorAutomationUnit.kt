package eu.automateeverything.coreplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.math.BigDecimal
import java.util.*

abstract class SinglePortControllerAutomationUnit<V: PortValue>(
    valueClass: Class<V>,
    nameOfOrigin: String,
    private val instanceDto: InstanceDto,
    private val controlPort: OutputPort<V>,
    readOnly: Boolean,
    private val stateChangeReporter: StateChangeReporter
) : ControllerAutomationUnit<V>(valueClass, nameOfOrigin) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override fun calculateInternal(now: Calendar) {
        val actualValue = controlPort.read()
        lastEvaluation = buildEvaluationResult(actualValue)
        stateChangeReporter.reportDeviceValueChange(this, instanceDto)
    }

    override var lastEvaluation = buildEvaluationResult(controlPort.read())

    private fun buildEvaluationResult(power: V) : EvaluationResult<V> {
        return EvaluationResult(
            interfaceValue = Resource.createUniResource("${power.asDecimal()} %"),
            value = power,
            isSignaled = power.asDecimal() > BigDecimal.ZERO,
            descriptions = lastNotes.values.toList()
        )
    }

    override val controlType = if (readOnly) ControlType.NA else ControlType.Controller

    override fun control(newValue: V, actor: String?) {
        val actualPowerLevel = controlPort.read()
        if (actualPowerLevel.asDecimal() != newValue.asDecimal()) {
            controlPort.write(newValue)
            lastEvaluation = buildEvaluationResult(newValue)
            stateChangeReporter.reportDeviceValueChange(this, instanceDto)
        }
    }
}

class PowerRegulatorAutomationUnit(
    nameOfOrigin: String,
    instanceDto: InstanceDto,
    controlPort: OutputPort<PowerLevel>,
    readOnly: Boolean,
    stateChangeReporter: StateChangeReporter,
) : SinglePortControllerAutomationUnit<PowerLevel>(PowerLevel::class.java, nameOfOrigin, instanceDto, controlPort, readOnly, stateChangeReporter) {
    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = 100.0.toBigDecimal()
    override val step: BigDecimal = 1.toBigDecimal()
}