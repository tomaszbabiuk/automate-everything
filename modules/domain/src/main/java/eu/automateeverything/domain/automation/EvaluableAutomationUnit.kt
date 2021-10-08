package eu.automateeverything.domain.automation

import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

interface EvaluableAutomationUnit {
    @Throws(Exception::class)
    fun evaluate(now: Calendar): Boolean
    val isPassed: Boolean
}