package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import java.math.BigDecimal
import java.util.*

open class AutomationUnitWrapper<T>(
    @Suppress("UNUSED_PARAMETER") valueClazz: Class<T>,
    name: String,
    initError: AutomationErrorException
) : AutomationUnitBase<T>(name, ControlType.NA) {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

    override var lastEvaluation = EvaluationResult<T>(
        interfaceValue = R.error_initialization,
        error = initError,
        descriptions = listOf(initError.localizedMessage)
    )
}

class StateDeviceAutomationUnitWrapper(
    name: String,
    initError: AutomationErrorException
) : AutomationUnitWrapper<State>(State::class.java, name, initError),
StateDeviceAutomationUnit{
    override fun changeState(state: String, actor: String?) {
    }

    override val currentState: State
        get() = ReadOnlyState("error",
                Resource.createUniResource("error"))

}

class ControllerAutomationUnitWrapper<V: PortValue>(
    override val valueClazz: Class<V>,
    name: String,
    initError: AutomationErrorException
) : AutomationUnitWrapper<V>(valueClazz, name, initError), ControllerAutomationUnit<V> {

    override var lastEvaluation = EvaluationResult<V>(
        interfaceValue = R.error_initialization,
        error = initError,
        descriptions = listOf(initError.localizedMessage)
    )

    override val usedPortsIds = arrayOf<String>()

    override fun calculateInternal(now: Calendar) {
    }

    override fun control(newValue: V, actor: String?) {
    }

    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = BigDecimal.ZERO
    override val step: BigDecimal = BigDecimal.ZERO
}
