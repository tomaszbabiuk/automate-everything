package eu.geekhome.automation

import java.util.*

class NotAutomationNode(
    private val nodeToNegate: ValueNode?,
) : ValueNode {

    override fun evaluate(now: Calendar): Boolean {

        if (nodeToNegate != null) {
            return !nodeToNegate.evaluate(now)
        }

        return false
    }
}