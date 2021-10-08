package eu.automateeverything.domain.automation

import java.util.*

class IsInStateAutomationNode(
    private val unit: StateDeviceAutomationUnitBase,
    private val observedStateId: String) : EvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {
        return (unit.currentState.id == observedStateId)
    }
}