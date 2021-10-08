package eu.automateeverything.domain.automation

import java.util.*

class ConditionAutomationNode(
    private val evaluator: EvaluableAutomationUnit
) : IEvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {
        evaluator.evaluate(now)
        return evaluator.isPassed
    }
}