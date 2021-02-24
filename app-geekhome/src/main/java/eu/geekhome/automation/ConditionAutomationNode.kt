package eu.geekhome.automation

import eu.geekhome.services.automation.EvaluableAutomationUnit

class ConditionAutomationNode(
    val conditionId: Long,
    evaluator: EvaluableAutomationUnit,
    override val next: AutomationNode?
) : AutomationNode {
    override fun process(timeInMillis: Long) {
    }
}