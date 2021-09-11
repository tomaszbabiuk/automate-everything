package eu.automateeverything.coreplugin

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.configurable.*
import org.pf4j.Extension
import java.util.HashMap

@Extension
class BashScriptActionConfigurable : ActionConfigurable() {

    override val parent: Class<out Configurable?>
        get() = ActionsConfigurable::class.java

    override val titleRes: Resource
        get() = R.configurable_bash_script_action_title

    override val descriptionRes: Resource
        get() = R.configurable_bash_script_action_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com" data-name="Layer 1">
             <title>big4_outline</title>
             <g class="layer">
              <title>terminal by Hare Krishna from the Noun Project</title>
              <path d="m87.83,12.83l-75.66,0a8,8 0 0 0 -8,8l0,54.67a8,8 0 0 0 8,8l75.66,0a8,8 0 0 0 8,-8l0,-54.67a8,8 0 0 0 -8,-8zm4,62.67a4,4 0 0 1 -4,4l-75.66,0a4,4 0 0 1 -4,-4l0,-54.67a4,4 0 0 1 4,-4l75.66,0a4,4 0 0 1 4,4l0,54.67zm-60,-40.33a2,2 0 0 1 0,2.83l-8.83,8.83a2,2 0 0 1 -2.83,-2.83l7.42,-7.42l-7.5,-7.5a2,2 0 0 1 2.83,-2.83l8.91,8.92zm21.17,10.25a2,2 0 0 1 -2,2l-14.5,0a2,2 0 0 1 0,-4l14.5,0a2,2 0 0 1 2,2z" id="svg_1"/>
             </g>
            </svg>
        """.trimIndent()

    override fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<State> {
        TODO("Not yet implemented")
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_READY] = State(
                STATE_READY,
                R.state_ready,
                R.state_ready,
                StateType.ReadOnly,
                isSignaled = false,
                codeRequired = false
            )
            states[STATE_EXECUTE] = State(
                STATE_EXECUTE,
                R.state_execute,
                R.state_execute,
                StateType.Control,
                isSignaled = false,
                codeRequired = false
            )
            states[STATE_RESET] = State(
                STATE_RESET,
                R.state_reset,
                R.state_reset,
                StateType.Control,
                isSignaled = false,
                codeRequired = false
            )
            return states
        }


    private val commandField = StringField(FIELD_COMMAND, R.field_command_hint, 0, "sudo shutdown -h 0", RequiredStringValidator())

    private val resetField = BooleanField(FIELD_RESET, R.field_reset_hint, 0, false)

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> =
                HashMap<String, FieldDefinition<*>>(super.fieldDefinitions)
            result[FIELD_COMMAND] = commandField
            result[FIELD_RESET] = resetField
            return result
        }
    override val addNewRes = R.configurable_bash_script_action_add

    override val editRes = R.configurable_bash_script_action_edit

    companion object {
        const val FIELD_COMMAND = "command"
        const val FIELD_RESET = "reset"
        const val STATE_READY = "ready"
        const val STATE_EXECUTE = "execute"
        const val STATE_RESET = "reset"
    }
}