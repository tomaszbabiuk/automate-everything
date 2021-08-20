package eu.geekhome.domain.automation

import java.util.*

class IsInStateAutomationNode(
    private val unit: StateDeviceAutomationUnit,
    private val observedStateId: String) : IEvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {
        return (unit.currentState.id == observedStateId)
    }
}