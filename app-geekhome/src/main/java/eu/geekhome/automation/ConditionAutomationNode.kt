package eu.geekhome.automation

import eu.geekhome.services.automation.IEvaluableAutomationUnit
import java.util.*

class ConditionAutomationNode(
    private val evaluator: IEvaluableAutomationUnit
) : ValueNode {

    override fun evaluate(now: Calendar): Boolean {
        evaluator.evaluate(now)
        return evaluator.isPassed
    }
}