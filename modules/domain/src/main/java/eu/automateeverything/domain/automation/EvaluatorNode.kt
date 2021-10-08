package eu.automateeverything.domain.automation

import java.util.*

interface EvaluatorNode: AutomationNode {
    fun evaluate(now: Calendar) : Boolean
}