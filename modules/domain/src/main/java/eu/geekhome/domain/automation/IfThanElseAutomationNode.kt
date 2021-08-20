package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import java.util.*

class IfThanElseAutomationNode(
    override val next: IStatementNode?,
    private val evaluatorNode: IEvaluatorNode?,
    private val ifNode: IStatementNode?,
    private val elseNode: IStatementNode?
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