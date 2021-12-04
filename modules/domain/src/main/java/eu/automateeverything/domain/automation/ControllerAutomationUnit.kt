package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal

abstract class ControllerAutomationUnit<V: PortValue>(
    val valueClazz: Class<V>, nameOfOrigin: String,
    readOnly: Boolean
) : AutomationUnit<V>(nameOfOrigin) {
    @Throws(Exception::class)
    abstract fun control(newValue: V, actor: String? = null)

    abstract val min: BigDecimal
    abstract val max: BigDecimal
    abstract val step: BigDecimal

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override val controlType = if (readOnly) ControlType.NA else ControlType.ControllerOther
}
