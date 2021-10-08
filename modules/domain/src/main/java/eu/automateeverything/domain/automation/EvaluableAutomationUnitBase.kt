package eu.automateeverything.domain.automation

import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

abstract class EvaluableAutomationUnitBase : EvaluableAutomationUnit {
    private var passed = false
    private var lastEvaluationTime: Long = 0

    override fun isPassed(): Boolean {
        return passed
    }

    @Throws(Exception::class)
    override fun evaluate(now: Calendar): Boolean {
        if (lastEvaluationTime != now.timeInMillis) {
            passed = doEvaluate(now)
            lastEvaluationTime = now.timeInMillis
        }
        return passed
    }

    @Throws(Exception::class)
    protected abstract fun doEvaluate(now: Calendar): Boolean
}