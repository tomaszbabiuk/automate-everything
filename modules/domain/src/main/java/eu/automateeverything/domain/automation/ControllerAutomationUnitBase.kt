package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import java.math.BigDecimal
import java.util.*

open class ControllerAutomationUnitBase<V : PortValue>(
    override val valueClazz: Class<V>,
    private val stateChangeReporter: StateChangeReporter,
    override val name: String,
    private val instance: InstanceDto,
    automationOnly: Boolean,
    override val min: BigDecimal,
    override val max: BigDecimal,
    override val step: BigDecimal,
    default: V,
) : AutomationUnitBase<V>(stateChangeReporter, name, instance, if (automationOnly) ControlType.NA else ControlType.ControllerOther),
    ControllerAutomationUnit<V> {

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override fun control(newValue: V, actor: String?) {
        val actualLevel = lastEvaluation.value
        if (actualLevel?.asDecimal() != newValue.asDecimal()) {
            lastEvaluation = buildEvaluationResult(newValue)
            stateChangeReporter.reportDeviceValueChange(this, instance)
        }
    }

    private fun buildEvaluationResult(level: V) : EvaluationResult<V> {
        return EvaluationResult(
            interfaceValue = level.toFormattedString(),
            value = level,
            descriptions = lastNotes.values.toList()
        )
    }

    override val usedPortsIds: Array<String> = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override var lastEvaluation: EvaluationResult<V> = buildEvaluationResult(default)
}