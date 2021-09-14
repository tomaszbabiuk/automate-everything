package eu.automateeverything.coreplugin

import eu.geekhome.data.automation.ControlState
import eu.geekhome.data.automation.ReadOnlyState
import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.configurable.ActionConfigurable
import eu.geekhome.domain.configurable.BooleanField
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.FieldDefinition
import java.util.HashMap

abstract class ActionConfigurableBase(
    private val stateChangeReporter: StateChangeReporter
) : ActionConfigurable() {

    override val parent: Class<out Configurable?>
        get() = ActionsConfigurable::class.java

    override fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<State> {
        val name = instance.fields[FIELD_NAME]!!
        return ActionAutomationUnit(stateChangeReporter, instance, name, states) {
            executionCode(instance)
        }
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_READY] = ControlState(
                STATE_READY,
                R.state_ready,
                R.action_reset
            )
            states[STATE_CANCELLED] = ControlState(
                STATE_CANCELLED,
                R.state_cancelled,
                R.action_cancel
            )
            states[STATE_SUCCESS] = ReadOnlyState(
                STATE_SUCCESS,
                R.state_success,
            )
            states[STATE_FAILURE] = ReadOnlyState(
                STATE_FAILURE,
                R.state_failure,
            )
            states[STATE_EXECUTING] = ControlState(
                STATE_EXECUTING,
                R.state_executing,
                R.action_execute
            )
            return states
        }

    protected abstract fun executionCode(instance: InstanceDto) : Pair<Boolean, Resource>

    companion object {
        const val STATE_READY = "ready"
        const val STATE_EXECUTING = "executed"
        const val STATE_SUCCESS = "success"
        const val STATE_FAILURE = "failure"
        const val STATE_CANCELLED = "cancelled"
    }
}