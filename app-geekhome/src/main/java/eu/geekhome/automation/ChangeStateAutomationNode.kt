package eu.geekhome.automation

import eu.geekhome.services.automation.DeviceAutomationUnit

class ChangeStateAutomationNode(
    val state: String,
    val evaluator: DeviceAutomationUnit<*>,
    override val next: AutomationNode?,
) : AutomationNode {
    override fun process(timeInMillis: Long) {
    }
}