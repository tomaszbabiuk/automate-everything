package eu.automateeverything.coreplugin

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
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
        val resetRequired = instance.fields[FIELD_RESET]!! == VALUE_TRUE
        return ActionAutomationUnit(stateChangeReporter, instance, name, resetRequired, states) {
            executionCode(instance)
        }
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = State(
                STATE_UNKNOWN,
                R.state_unknown,
                R.state_unknown,
                StateType.ReadOnly,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_READY] = State(
                STATE_READY,
                R.state_ready,
                R.state_reset,
                StateType.ReadOnly,
                isSignaled = false,
                codeRequired = false
            )
            states[STATE_EXECUTED] = State(
                STATE_EXECUTED,
                R.state_executed,
                R.action_execute,
                StateType.Control,
                isSignaled = false,
                codeRequired = false
            )
            return states
        }

    private val resetField = BooleanField(FIELD_RESET, R.field_reset_hint, 0, false)

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> =
                LinkedHashMap(super.fieldDefinitions)
            addExtraFields(result)
            result[FIELD_RESET] = resetField
            return result
        }

    protected abstract fun addExtraFields(result: MutableMap<String, FieldDefinition<*>>)

    protected abstract fun executionCode(instance: InstanceDto) : String

    companion object {
        const val FIELD_RESET = "reset"
        const val STATE_READY = "ready"
        const val STATE_EXECUTED = "executed"
        const val VALUE_TRUE = "1"
    }
}