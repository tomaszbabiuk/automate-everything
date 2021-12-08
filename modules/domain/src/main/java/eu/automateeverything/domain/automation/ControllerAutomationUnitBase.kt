package eu.automateeverything.domain.automation

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue

abstract class ControllerAutomationUnitBase<V : PortValue>(
    val name: String,
    automationOnly: Boolean
) : AutomationUnitBase<V>(name, if (automationOnly) ControlType.NA else ControlType.ControllerOther),
    ControllerAutomationUnit<V> {

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}