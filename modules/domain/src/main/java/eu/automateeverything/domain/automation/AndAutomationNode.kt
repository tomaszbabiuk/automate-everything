package eu.automateeverything.domain.automation

import java.util.*

class AndAutomationNode(
    private val firstEvaluator: EvaluatorNode?,
    private val secondEvaluator: EvaluatorNode?
) : EvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {

        var firstPassed = false
        var secondPassed = false
        if (firstEvaluator != null) {
            firstPassed = firstEvaluator.evaluate(now)
        }

        if (secondEvaluator != null) {
            secondPassed = secondEvaluator.evaluate(now)
        }


        return firstPassed && secondPassed
    }
}