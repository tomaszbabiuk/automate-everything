package eu.automateeverything.domain.automation

import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal

interface ControllerAutomationUnit<V: PortValue> : AutomationUnit<V> {
    val valueClazz: Class<V>
    fun control(newValue: V, actor: String? = null)

    val min: BigDecimal
    val max: BigDecimal
    val step: BigDecimal
}

