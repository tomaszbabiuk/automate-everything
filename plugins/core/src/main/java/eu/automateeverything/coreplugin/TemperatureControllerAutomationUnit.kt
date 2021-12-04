package eu.automateeverything.coreplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.hardware.Temperature
import java.math.BigDecimal
import java.util.*

class TemperatureControllerAutomationUnit(
    nameOfOrigin: String,
    override val min: BigDecimal,
    override val max: BigDecimal,
    default: BigDecimal,
    private val instanceDto: InstanceDto,
    readOnly: Boolean,
    private val stateChangeReporter: StateChangeReporter
) : ControllerAutomationUnit<Temperature>(Temperature::class.java, nameOfOrigin, readOnly) {

    override val step: BigDecimal = (0.05).toBigDecimal()
    override val usedPortsIds: Array<String> = arrayOf()

    override val controlType = if (readOnly) ControlType.NA else ControlType.ControllerTemperature

    override fun calculateInternal(now: Calendar) {
    }

    override var lastEvaluation: EvaluationResult<Temperature> = buildEvaluationResult(Temperature(default))

    private fun buildEvaluationResult(level: Temperature) : EvaluationResult<Temperature> {
        return EvaluationResult(
            interfaceValue = level.toFormattedString(),
            value = level,
            descriptions = lastNotes.values.toList()
        )
    }

    override fun control(newValue: Temperature, actor: String?) {
        val actualLevel = lastEvaluation.value
        if (actualLevel?.asDecimal() != newValue.asDecimal()) {
            lastEvaluation = buildEvaluationResult(newValue)
            stateChangeReporter.reportDeviceValueChange(this, instanceDto)
        }
    }
}