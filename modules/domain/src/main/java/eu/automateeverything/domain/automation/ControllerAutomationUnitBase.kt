package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue

abstract class ControllerAutomationUnitBase<V: PortValue>(
    val name: String,
    val readOnly: Boolean
    ) : AutomationUnitBase<V>(name, if (readOnly) ControlType.NA else ControlType.ControllerOther),
    ControllerAutomationUnit<V> {

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}