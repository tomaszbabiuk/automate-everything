package eu.geekhome.coreplugin

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.hardware.IPortFinder
import eu.geekhome.domain.hardware.Relay
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.StateChangeReporter
import org.pf4j.Extension
import java.util.*

@Extension
class TimedOnOffDeviceConfigurable : StateDeviceConfigurable() {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            result[FIELD_MIN_TIME] = minTimeField
            result[FIELD_MAX_TIME] = maxTimeField
            result[FIELD_BREAK_TIME] = breakTimeField
            return result
        }

    override val parent: Class<out Configurable?>
        get() = DevicesConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_timedonoffdevice_add

    override val editRes: Resource
        get() = R.configurable_timedonoffdevice_edit

    override val titleRes: Resource
        get() = R.configurable_timedonoffdevice_title

    override val descriptionRes: Resource
        get() = R.configurable_timedonoffdevices_description

    override val iconRaw: String
        get() = """<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
                 <g>
                  <title>Layer 1</title>
                  <path fill="black" d="m4,53.572c-0.016,-12.872 5.321,-22.61 10.48,-28.792l0,0c5.187,-6.233 10.224,-9.236 10.676,-9.516l0,0c3.368,-1.993 7.697,-0.849 9.667,2.555l0,0c1.966,3.392 0.851,7.747 -2.489,9.75l0,0l0.001,0.001c0,0 -0.004,0.001 -0.015,0.008l0,0c-0.009,0.004 -0.023,0.014 -0.042,0.026l0,0c-0.068,0.042 -0.194,0.125 -0.367,0.244l0,0c-0.351,0.234 -0.897,0.625 -1.572,1.162l0,0c-1.349,1.073 -3.203,2.74 -5.029,4.945l0,0c-3.669,4.467 -7.166,10.798 -7.181,19.616l0,0c0.003,8.889 3.547,16.882 9.314,22.722l0,0c5.78,5.83 13.687,9.416 22.48,9.418l0,0c8.793,-0.002 16.699,-3.588 22.478,-9.418l0,0c5.769,-5.84 9.313,-13.833 9.316,-22.722l0,0c-0.01601,-8.548 -3.29401,-14.743 -6.83701,-19.195l0,0c-3.37299,-4.221 -6.98199,-6.572 -7.341,-6.791l0,0c-0.004,-0.003 -0.009,-0.004 -0.012,-0.006l0,0c-0.005,-0.003 -0.008,-0.005 -0.01099,-0.006l0,0c-0.00301,-0.003 -0.007,-0.003 -0.007,-0.003l0,0l0,0c-3.336,-2.005 -4.452,-6.36 -2.488,-9.752l0,0c1.972,-3.404 6.3,-4.548 9.667,-2.555l0,0c0.45399,0.279 5.491,3.282 10.67799,9.516l0,0c5.16199,6.183 10.49599,15.92 10.481,28.792l0,0c-0.004,25.637 -20.564,46.42 -45.923,46.429l0,0c-25.359,-0.009 -45.918,-20.792 -45.924,-46.428l0,0l0.00002,0z" id="svg_1"/>
                  <path d="m42.859,50.001l0,-21.43l0,-21.428c0,-3.944 3.163,-7.143 7.066,-7.143l0,0c3.899,0 7.062,3.199 7.062,7.143l0,0l0,21.428l0,21.43l0.002,0c0,3.941 -3.165,7.141 -7.064,7.141l0,0c-3.903,0 -7.066,-3.2 -7.066,-7.141l0,0z" id="svg_2"/>
                 </g>
                </svg>"""

    private val portField = RelayOutputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())
    private val minTimeField = DurationField(FIELD_MIN_TIME, R.field_min_working_time, Duration(0))
    private val maxTimeField = DurationField(FIELD_MAX_TIME, R.field_max_working_time, Duration(0))
    private val breakTimeField = DurationField(FIELD_BREAK_TIME, R.field_break_time, Duration(0))

    override fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder, stateChangeReporter: StateChangeReporter): DeviceAutomationUnit<State> {
        val portId = readPortId(instance)
        val port = portFinder.searchForOutputPort(Relay::class.java, portId)
        val name = instance.fields[FIELD_NAME]!!
        val minWorkingTime = minTimeField.builder.fromPersistableString(instance.fields[FIELD_MIN_TIME])
        val maxWorkingTime = maxTimeField.builder.fromPersistableString(instance.fields[FIELD_MAX_TIME])
        val breakTime = breakTimeField.builder.fromPersistableString(instance.fields[FIELD_BREAK_TIME])
        return TimedOnOffDeviceAutomationUnit(
            stateChangeReporter,
            instance,
            name,
            minWorkingTime,
            maxWorkingTime,
            breakTime,
            states,
            port)
    }

    private fun readPortId(instance: InstanceDto): String {
        val portFieldValue = instance.fields[FIELD_PORT]
        return portField.builder.fromPersistableString(portFieldValue)
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
            states[STATE_ON] = State(
                STATE_ON,
                R.state_on,
                R.state_on,
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_ON_COUNTING] = State(
                STATE_ON_COUNTING,
                R.state_on_counting,
                R.state_on,
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_OFF_BREAK] = State(
                STATE_OFF_BREAK,
                R.state_off_break,
                R.state_off_break,
                StateType.ReadOnly,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_OFF] = State(
                STATE_OFF,
                R.state_forced_off,
                R.state_off,
                StateType.Control,
                isSignaled = false,
                codeRequired = false
            )
            return states
        }

    companion object {
        const val FIELD_MIN_TIME = "minTime"
        const val FIELD_MAX_TIME = "maxTime"
        const val FIELD_BREAK_TIME = "breakTime"
        const val FIELD_PORT = "portId"
        const val STATE_ON = "on"
        const val STATE_ON_COUNTING = "on_counting"
        const val STATE_OFF = "off"
        const val STATE_OFF_BREAK = "off_break"
    }
}