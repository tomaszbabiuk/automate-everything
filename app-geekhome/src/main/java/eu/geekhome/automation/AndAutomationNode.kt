package eu.geekhome.automation

import java.util.*

class AndAutomationNode(
    private val firstEvaluator: ValueNode?,
    private val secondEvaluator: ValueNode?
) : ValueNode {

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