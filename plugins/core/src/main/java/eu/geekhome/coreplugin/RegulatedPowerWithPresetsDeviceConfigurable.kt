package eu.geekhome.coreplugin

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.hardware.IPortFinder
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.automation.StateChangeReporter
import eu.geekhome.domain.hardware.PowerLevel
import org.pf4j.Extension
import kotlin.collections.LinkedHashMap

@Extension
class RegulatedPowerWithPresetsDeviceConfigurable : StateDeviceConfigurable() {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            result[FIELD_PRESET1] = preset1Field
            result[FIELD_PRESET2] = preset2Field
            result[FIELD_PRESET3] = preset3Field
            result[FIELD_PRESET4] = preset4Field
            return result
        }

    override val parent: Class<out Configurable?>
        get() = DevicesConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_powerdevice_add

    override val editRes: Resource
        get() = R.configurable_powerdevice_edit

    override val titleRes: Resource
        get() = R.configurable_powerdevice_title

    override val descriptionRes: Resource
        get() = R.configurable_powerdevices_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
                <g class="layer">
                    <title>Layer 1</title>
                    <g id="svg_1" transform="translate(0,-952.36218)">
                        <g id="svg_2" transform="translate(20.998031,959.86021)">
                            <path d="m63.7128,20.68252c-28.04895,13.1083 -44.7142,25.167 -72.13318,38.0516c-2.83588,1.7489 -1.50598,5.6628 1.28362,5.7313c30.03018,-0.01701 40.25935,0.09 72.13318,0c1.57366,-0.00021 3.00539,-1.4323 3.00555,-3.0065l0,-38.0517c-0.76447,-2.4085 -2.19719,-3.3305 -4.28917,-2.7247zm-37.78851,26.7771l0,11.024c-6.63822,-0.01 -13.09889,-0.026 -20.44399,-0.031c7.24335,-3.7115 13.87866,-7.3556 20.44399,-10.9927l0,-0.0003z" fill="#000000" fill-rule="nonzero" id="svg_3"/>
                        </g>
                    </g>
                </g>
            </svg>
        """.trimIndent()

    private val portField = PowerLevelOutputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())
    private val preset1Field = PowerLevelField(FIELD_PRESET1, R.field_preset1_hint, 25)
    private val preset2Field = PowerLevelField(FIELD_PRESET2, R.field_preset2_hint, 50)
    private val preset3Field = PowerLevelField(FIELD_PRESET3, R.field_preset3_hint, 75)
    private val preset4Field = PowerLevelField(FIELD_PRESET4, R.field_preset4_hint, 100)

    override fun buildAutomationUnit(
        instance: InstanceDto,
        portFinder: IPortFinder,
        stateChangeReporter: StateChangeReporter): DeviceAutomationUnit<State> {

        val portId = readPortId(instance)
        val port = portFinder.searchForOutputPort(PowerLevel::class.java, portId)
        val name = instance.fields[FIELD_NAME]!!
        val preset1 = instance.fields[FIELD_PRESET1]!!.toInt()
        val preset2 = instance.fields[FIELD_PRESET2]!!.toInt()
        val preset3 = instance.fields[FIELD_PRESET3]!!.toInt()
        val preset4 = instance.fields[FIELD_PRESET4]!!.toInt()
        return RegulatedPowerWithPresetsDeviceAutomationUnit(stateChangeReporter, instance, name, preset1, preset2, preset3, preset4, states, port)
    }

    private fun readPortId(instance: InstanceDto): String {
        val portFieldValue = instance.fields[FIELD_PORT]
        return portField.builder.fromPersistableString(portFieldValue)
    }

    override val states: Map<String, State>
        get() {
            val states: LinkedHashMap<String, State> = LinkedHashMap()
            states[STATE_UNKNOWN] = State(
                STATE_UNKNOWN,
                R.state_unknown,
                R.state_unknown,
                StateType.ReadOnly,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_PRESET1] = State(
                STATE_PRESET1,
                R.state_preset1,
                Resource.createUniResource("1"),
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_PRESET2] = State(
                STATE_PRESET2,
                R.state_preset2,
                Resource.createUniResource("2"),
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_PRESET3] = State(
                STATE_PRESET3,
                R.state_preset3,
                Resource.createUniResource("3"),
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_PRESET4] = State(
                STATE_PRESET4,
                R.state_preset4,
                Resource.createUniResource("4"),
                StateType.Control,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_OFF] = State(
                STATE_OFF,
                R.state_off,
                R.state_off,
                StateType.Control,
                isSignaled = false,
                codeRequired = false
            )
            states[STATE_MANUAL] = State(
                STATE_MANUAL,
                R.state_manual,
                R.state_manual,
                StateType.ReadOnly,
                isSignaled = true,
                codeRequired = false
            )
            return states
        }

    companion object {
        const val FIELD_PRESET1 = "preset1"
        const val FIELD_PRESET2 = "preset2"
        const val FIELD_PRESET3 = "preset3"
        const val FIELD_PRESET4 = "preset4"
        const val FIELD_PORT = "portId"
        const val STATE_OFF = "off"
        const val STATE_PRESET1 = "preset1"
        const val STATE_PRESET2 = "preset2"
        const val STATE_PRESET3 = "preset3"
        const val STATE_PRESET4 = "preset4"
        const val STATE_MANUAL = "manual"
    }
}