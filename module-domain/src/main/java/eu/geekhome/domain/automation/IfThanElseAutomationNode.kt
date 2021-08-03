package eu.geekhome.domain.automation

import java.util.*

class IfThanElseAutomationNode(
    override val next: IStatementNode?,
    private val evaluatorNode: IEvaluatorNode?,
    private val ifNode: IStatementNode?,
    private val elseNode: IStatementNode?
    ) : IStatementNode {

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (evaluatorNode!= null && evaluatorNode.evaluate(now)) {
            ifNode?.process(now, firstLoop)
        } else {
            elseNode?.process(now, firstLoop)
        }

        next?.process(now, firstLoop)
    }
}