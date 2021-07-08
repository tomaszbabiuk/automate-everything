package eu.geekhome.domain.automation

import java.util.*

class IfThanElseAutomationNode(
    override val next: IStatementNode?,
    private val evaluatorNode: IEvaluatorNode?,
    private val ifNode: IStatementNode?,
    private val elseNode: IStatementNode?
    ) : IStatementNode {

    override fun process(now: Calendar) {
        if (evaluatorNode!= null && evaluatorNode.evaluate(now)) {
            ifNode?.process(now)
        } else {
            elseNode?.process(now)
        }

        next?.process(now)
    }
}