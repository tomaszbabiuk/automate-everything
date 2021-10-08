package eu.automateeverything.domain.automation

import java.util.*

class NotAutomationNode(
    private val nodeToNegate: EvaluatorNode?,
) : EvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {

        if (nodeToNegate != null) {
            return !nodeToNegate.evaluate(now)
        }

        return false
    }
}