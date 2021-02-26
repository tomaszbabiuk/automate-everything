package eu.geekhome.automation

import eu.geekhome.services.automation.EvaluableAutomationUnit
import java.util.*

class ConditionAutomationNode(
    private val evaluator: EvaluableAutomationUnit
) : ValueNode {

    override fun evaluate(now: Calendar): Boolean {
        evaluator.evaluate(now)
        return evaluator.isPassed
    }
}