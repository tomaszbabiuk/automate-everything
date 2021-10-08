package eu.automateeverything.domain.automation

import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

abstract class EvaluableAutomationUnitBase : EvaluableAutomationUnit {
    private var lastEvaluationTime: Long = 0

    override var isPassed = false

    @Throws(Exception::class)
    override fun evaluate(now: Calendar): Boolean {
        if (lastEvaluationTime != now.timeInMillis) {
            isPassed = doEvaluate(now)
            lastEvaluationTime = now.timeInMillis
        }
        return isPassed
    }

    @Throws(Exception::class)
    protected abstract fun doEvaluate(now: Calendar): Boolean
}