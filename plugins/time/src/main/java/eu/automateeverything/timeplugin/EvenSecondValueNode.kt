package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.EvaluatorNode
import java.util.*

class EvenSecondValueNode : EvaluatorNode {

    override fun evaluate(now: Calendar): Boolean {
        val second = now.get(Calendar.SECOND)
        return second % 2 == 0
    }
}