package eu.geekhome.automation

import java.util.*

class IfThanElseAutomationNode(
    override val next: StatementNode?,
    private val valueNode: ValueNode?,
    private val ifNode: StatementNode?,
    private val elseNode: StatementNode?
    ) : StatementNode {

    override fun process(now: Calendar) {
        if (valueNode!= null && valueNode.evaluate(now)) {
            ifNode?.process(now)
        } else {
            elseNode?.process(now)
        }

        next?.process(now)
    }
}