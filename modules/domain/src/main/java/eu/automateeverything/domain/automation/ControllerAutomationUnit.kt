package eu.automateeverything.domain.automation

import eu.automateeverything.data.hardware.PortValue
import java.math.BigDecimal

abstract class ControllerAutomationUnit<V: PortValue>(val valueClazz: Class<V>, nameOfOrigin: String) : AutomationUnit<V>(nameOfOrigin) {
    @Throws(Exception::class)
    abstract fun control(newValue: V, actor: String? = null)

    abstract val min: BigDecimal
    abstract val max: BigDecimal
    abstract val step: BigDecimal
}
