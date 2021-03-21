package eu.geekhome.automation

import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import java.util.*

class AutomationUnitWrapper<T>(
    valueType: Class<T>
) : IDeviceAutomationUnit<T>(valueType) {

    override fun calculate(now: Calendar) {
    }

    override fun buildEvaluationResultInternally(): EvaluationResult {
        TODO("Not yet implemented")
    }

    override val value: T?
        get() = null
}