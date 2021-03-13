package eu.geekhome.automation

import eu.geekhome.services.R
import eu.geekhome.services.automation.EvaluationResult
import eu.geekhome.services.automation.IDeviceAutomationUnit
import eu.geekhome.services.repository.InstanceDto
import java.util.*

class AutomationUnitWrapper<T>(
    val instance: InstanceDto,
    var state: UnitState = UnitState.Operational,
    var error: Exception? = null,
    val wrapped: IDeviceAutomationUnit<T>? = null,
) : IDeviceAutomationUnit<T> {

    override fun calculate(now: Calendar?) {
        wrapped?.calculate(now)
    }

    override val value: T?
        get() = wrapped?.value

    override fun buildEvaluationResult() : EvaluationResult {
        return when (state) {
            UnitState.InitError -> buildInitErrorEvaluationResult()
            UnitState.AutomationError -> buildAutomationErrorEvaluationResult()
            UnitState.Operational -> {
                if (wrapped != null) {
                    try {
                        wrapped.buildEvaluationResult()
                    } catch (ex: Exception) {
                        state = UnitState.AutomationError
                        error = ex
                        return buildEvaluationResult()
                    }
                } else {
                    throw IllegalStateException()
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

enum class UnitState {
    InitError,
    AutomationError,
    Operational
}