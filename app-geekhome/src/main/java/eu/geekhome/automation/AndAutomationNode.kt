package eu.geekhome.automation

import java.util.*

class AndAutomationNode(
    private val firstEvaluator: IEvaluatorNode?,
    private val secondEvaluator: IEvaluatorNode?
) : IEvaluatorNode {

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