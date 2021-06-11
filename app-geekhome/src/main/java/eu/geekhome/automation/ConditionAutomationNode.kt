package eu.geekhome.automation

import eu.geekhome.domain.automation.IEvaluableAutomationUnit
import java.util.*

class ConditionAutomationNode(
    private val evaluator: IEvaluableAutomationUnit
) : IEvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {
        evaluator.evaluate(now)
        return evaluator.isPassed
    }
}