package eu.geekhome.coreplugin

import eu.geekhome.data.automation.State
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.hardware.IPortFinder
import eu.geekhome.domain.hardware.Relay
import eu.geekhome.data.localization.Resource
import org.pf4j.Extension
import java.util.*

@Extension
class PowerDeviceConfigurable : StateDeviceConfigurable() {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
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

    override fun buildAutomationUnit(instance: InstanceDto, portFinder: IPortFinder): DeviceAutomationUnit<State> {
        val portId = readPortId(instance)
        val port = portFinder.searchForOutputPort(Relay::class.java, portId)
        val name = instance.fields["name"]
        return OnOffDeviceAutomationUnit(name, states, STATE_ON, port)
    }

    private fun readPortId(instance: InstanceDto): String {
        val portFieldValue = instance.fields[FIELD_PORT]
        return portField.builder.fromPersistableString(portFieldValue)
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_ON] = State(
                STATE_ON,
                R.state_on,
                eu.geekhome.data.automation.StateType.NonSignaledAction,
                isSignaled = true,
                codeRequired = false
            )
            states[STATE_OFF] = State(
                STATE_OFF,
                R.state_off,
                eu.geekhome.data.automation.StateType.SignaledAction,
                isSignaled = false,
                codeRequired = false
            )
            return states
        }

    companion object {
        const val FIELD_PORT = "portId"
        const val STATE_ON = "on"
        const val STATE_OFF = "off"
    }
}