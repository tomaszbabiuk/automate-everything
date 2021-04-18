package eu.geekhome.services.automation

import eu.geekhome.services.R

enum class UnitCondition {
    InitError,
    AutomationError,
    Operational
}

abstract class IDeviceAutomationUnit<T>(
    val valueType: Class<T>,
    var condition: UnitCondition = UnitCondition.Operational,
    var error: Exception? = null,
) : ICalculableAutomationUnit {

    abstract fun buildEvaluationResultInternally(): EvaluationResult

    abstract val value: T?

    abstract val usedPortsIds: Array<String>

    fun buildEvaluationResult(): EvaluationResult {
        return when (condition) {
            UnitCondition.InitError -> buildInitErrorEvaluationResult()
            UnitCondition.AutomationError -> buildAutomationErrorEvaluationResult()
            UnitCondition.Operational -> {
                return try {
                    buildEvaluationResultInternally()
                } catch (ex: Exception) {
                    condition = UnitCondition.AutomationError
                    error = ex
                    buildEvaluationResult()
                }
            }
        }
    }

    private fun buildAutomationErrorEvaluationResult(): EvaluationResult {
        return EvaluationResult("automationError", R.error_automation, false, null)
    }

    private fun buildInitErrorEvaluationResult(): EvaluationResult {
        return EvaluationResult("initializationError", R.error_initialization, false, null)
    }
}