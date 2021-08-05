package eu.geekhome.domain.automation

import eu.geekhome.data.localization.Resource
import java.util.*

class IfThanElseAutomationNode(
    override val next: IStatementNode?,
    private val evaluatorNode: IEvaluatorNode?,
    private val ifNode: IStatementNode?,
    private val elseNode: IStatementNode?
    ) : IStatementNode {

    override fun process(now: Calendar, firstLoop: Boolean, notes: MutableList<Resource>) {
        if (evaluatorNode!= null && evaluatorNode.evaluate(now)) {
            ifNode?.process(now, firstLoop, notes)
        } else {
            elseNode?.process(now, firstLoop, notes)
        }

        next?.process(now, firstLoop, notes)
    }
}