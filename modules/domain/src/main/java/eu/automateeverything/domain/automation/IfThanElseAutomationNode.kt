package eu.automateeverything.domain.automation

import java.util.*

class IfThanElseAutomationNode(
    override val next: StatementNode?,
    private val evaluatorNode: EvaluatorNode?,
    private val ifNode: StatementNode?,
    private val elseNode: StatementNode?
    ) : StatementNodeBase() {

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (evaluatorNode!= null && evaluatorNode.evaluate(now)) {
            ifNode?.process(now, firstLoop)
        } else {
            elseNode?.process(now, firstLoop)
        }

        next?.process(now, firstLoop)
    }
}