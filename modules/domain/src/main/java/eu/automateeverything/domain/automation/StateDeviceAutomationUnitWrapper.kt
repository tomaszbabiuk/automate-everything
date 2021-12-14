package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import java.math.BigDecimal
import java.util.*

open class AutomationUnitWrapper<T>(
    @Suppress("UNUSED_PARAMETER") valueClazz: Class<T>,
    val stateChangeReporter: StateChangeReporter,
    name: String,
    val instance: InstanceDto,
    initError: AutomationErrorException
) : AutomationUnitBase<T>(stateChangeReporter, name, instance, ControlType.NA) {

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
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    initError: AutomationErrorException
) : AutomationUnitWrapper<State>(State::class.java, stateChangeReporter, name, instance, initError),
StateDeviceAutomationUnit{
    override fun changeState(state: String, actor: String?) {
    }

    override val currentState: State
        get() = ReadOnlyState("error",
                Resource.createUniResource("error"))

}

class ControllerAutomationUnitWrapper<V: PortValue>(
    override val valueClazz: Class<V>,
    stateChangeReporter: StateChangeReporter,
    name: String,
    instance: InstanceDto,
    initError: AutomationErrorException
) : AutomationUnitWrapper<V>(valueClazz, stateChangeReporter, name, instance, initError), ControllerAutomationUnit<V> {

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
