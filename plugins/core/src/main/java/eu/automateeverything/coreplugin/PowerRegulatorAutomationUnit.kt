package eu.automateeverything.coreplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.math.BigDecimal
import java.util.*

class PowerRegulatorAutomationUnit(
    instanceDto: InstanceDto,
    nameOfOrigin: String,
    private val controlPort: OutputPort<PowerLevel>,
    readOnly: Boolean
) : ControllerAutomationUnit<PowerLevel>(PowerLevel::class.java, nameOfOrigin) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override var lastEvaluation = buildEvaluationResult(controlPort.read())

    override fun calculateInternal(now: Calendar) {
        //TODO: recalculate on port update
    }

    private fun buildEvaluationResult(power: PowerLevel) : EvaluationResult<PowerLevel> {
        return EvaluationResult(
            interfaceValue = Resource.createUniResource("${power.value} %"),
            value = power,
            isSignaled = power.value > BigDecimal.ZERO,
            descriptions = lastNotes.values.toList()
        )
    }

    override val controlType = if (readOnly) ControlType.NA else ControlType.Controller

    override fun control(newValue: PowerLevel, actor: String?) {
        val actualPowerLevel = controlPort.read()
        if (actualPowerLevel.value != newValue.value) {
            controlPort.write(newValue)
            lastEvaluation = buildEvaluationResult(newValue)
            //TODO: Report change in state reporter
        }
    }

    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = 100.0.toBigDecimal()
    override val step: BigDecimal = 0.5.toBigDecimal()
}