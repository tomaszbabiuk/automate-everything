package eu.automateeverything.alarmplugin

import eu.automateeverything.data.automation.ControlState
import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.DeviceAutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.hardware.Relay
import org.pf4j.Extension

@Extension
class CombinationLockConfigurable<T: PortValue>(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {

    private val statusPortField = RelayOutputPortField(FIELD_STATUS_PORT, R.field_status_port_hint, RequiredStringValidator())
    private val armingPortField = BinaryInputPortField(FIELD_ARMING_PORT, R.field_arming_port_hint, RequiredStringValidator())

    override fun buildAutomationUnit(instance: InstanceDto): DeviceAutomationUnit<State> {
        val armingPortId = extractFieldValue(instance, armingPortField)
        val armingPort = portFinder.searchForInputPort(BinaryInput::class.java, armingPortId)
        val statusPortId = extractFieldValue(instance, armingPortField)
        val statusPort = portFinder.searchForOutputPort(Relay::class.java, statusPortId)

        val name = extractFieldValue(instance, nameField)
        return CombinationLockAutomationUnit(stateChangeReporter, instance, name, states, armingPort, statusPort)
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_LEAVING] = ReadOnlyState(
                STATE_LEAVING,
                R.state_leaving,
            )
            states[STATE_DISARMED] = ControlState(
                STATE_DISARMED,
                R.state_disarmed,
                R.action_arm
            )
            states[STATE_ARMED] = ControlState(
                STATE_ARMED,
                R.state_armed,
                R.action_disarm,
                isSignaled = true
            )
            return states
        }

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_STATUS_PORT] = statusPortField
            result[FIELD_ARMING_PORT] = armingPortField
            return result
        }

    override val parent: Class<out Configurable>? = null

    override val addNewRes = R.configurable_combinationlock_add
    override val editRes = R.configurable_combinationlock_edit
    override val titleRes = R.configurable_combintationlocks_title
    override val descriptionRes = R.configurable_combinationlocks_description

    override val iconRaw: String
        get() = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="100px" height="100px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
            <g>
                <path fill="#000000" d="M24.666,2.5H5.334C3.769,2.5,2.5,3.769,2.5,5.334v19.331c0,1.565,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.834-1.269,2.834-2.834V5.334C27.5,3.769,26.231,2.5,24.666,2.5z"/>
                <path fill="#000000" d="M59.704,2.5H40.373c-1.565,0-2.834,1.269-2.834,2.834v19.331c0,1.565,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.835-1.269,2.835-2.834V5.334C62.539,3.769,61.27,2.5,59.704,2.5z"/>
                <path fill="#000000" d="M94.742,2.5h-19.33c-1.566,0-2.835,1.269-2.835,2.834v19.331c0,1.565,1.269,2.834,2.835,2.834h19.33   c1.566,0,2.836-1.269,2.836-2.834V5.334C97.578,3.769,96.309,2.5,94.742,2.5z"/>
                <path fill="#000000" d="M24.666,37.667H5.334c-1.565,0-2.834,1.269-2.834,2.834v19.331c0,1.565,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.834-1.269,2.834-2.834V40.501C27.5,38.936,26.231,37.667,24.666,37.667z"/>
                <path fill="#000000" d="M59.704,37.667H40.373c-1.565,0-2.834,1.269-2.834,2.834v19.331c0,1.565,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.835-1.269,2.835-2.834V40.501C62.539,38.936,61.27,37.667,59.704,37.667z"/>
                <path fill="#000000" d="M94.742,37.667h-19.33c-1.566,0-2.835,1.269-2.835,2.834v19.331c0,1.565,1.269,2.834,2.835,2.834h19.33   c1.566,0,2.836-1.269,2.836-2.834V40.501C97.578,38.936,96.309,37.667,94.742,37.667z"/>
                <path fill="#000000" d="M24.666,72.833H5.334c-1.565,0-2.834,1.269-2.834,2.835v19.33c0,1.566,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.834-1.268,2.834-2.834v-19.33C27.5,74.102,26.231,72.833,24.666,72.833z"/>
                <path fill="#000000" d="M59.704,72.833H40.373c-1.565,0-2.834,1.269-2.834,2.835v19.33c0,1.566,1.269,2.834,2.834,2.834h19.331   c1.565,0,2.835-1.268,2.835-2.834v-19.33C62.539,74.102,61.27,72.833,59.704,72.833z"/>
                <path fill="#000000" d="M94.742,72.833h-19.33c-1.566,0-2.835,1.269-2.835,2.835v19.33c0,1.566,1.269,2.834,2.835,2.834h19.33   c1.566,0,2.836-1.268,2.836-2.834v-19.33C97.578,74.102,96.309,72.833,94.742,72.833z"/>
            </g>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_ARMING_PORT = "armingPortId"
        const val FIELD_STATUS_PORT = "statusPortId"

        const val STATE_ARMED = "armed"
        const val STATE_DISARMED = "disarmed"
        const val STATE_LEAVING = "leaving"
    }
}

