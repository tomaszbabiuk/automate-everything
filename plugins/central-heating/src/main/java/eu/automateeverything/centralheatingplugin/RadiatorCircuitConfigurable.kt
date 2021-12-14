package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.automation.ControlState
import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.fields.InstanceReference
import eu.automateeverything.data.fields.InstanceReferenceType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.sensorsandcontrollersplugin.TemperatureControllerConfigurable
import eu.automateeverything.sensorsandcontrollersplugin.ThermometerConfigurable
import org.pf4j.Extension

@Extension
class RadiatorCircuitConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {

    override val hasAutomation = true
    override val editableIcon = true
    override val taggable = true

    override val parent: Class<out Configurable> = CentralHeatingConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_radiator_circuit_add

    override val editRes: Resource
        get() = R.configurable_radiator_circuit_edit

    override val titleRes: Resource
        get() = R.configurable_radiator_circuits_title

    override val descriptionRes: Resource
        get() = R.configurable_radiator_circuits_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_ACTUATOR_PORT] = actuatorPortField
            result[FIELD_THERMOMETER_ID] = thermometerIdField
            result[FIELD_TEMPERATURE_CONTROLLER_ID] = temperatureControllerIdField
            result[FIELD_OPENING_TIME] = openingTimeField
            result[FIELD_INACTIVE_STATE] = inactiveStateField
            return result
        }

    private val openingTimeField = DurationField(FIELD_OPENING_TIME, R.field_opening_time_hint,
        Duration(120)
    )

    private val thermometerIdField = InstanceReferenceField(
        FIELD_THERMOMETER_ID, R.field_thermometer_hint,
        InstanceReference(ThermometerConfigurable::class.java, InstanceReferenceType.Single),
        RequiredStringValidator()
    )

    private val temperatureControllerIdField = InstanceReferenceField(
        FIELD_TEMPERATURE_CONTROLLER_ID, R.field_temperature_controller_hint,
        InstanceReference(TemperatureControllerConfigurable::class.java, InstanceReferenceType.Single),
        RequiredStringValidator()
    )

    private val actuatorPortField = RelayOutputPortField(FIELD_ACTUATOR_PORT, R.field_actuator_port_hint, RequiredStringValidator())

    private val inactiveStateField = ContactTypeField(FIELD_INACTIVE_STATE, R.field_inactive_state_hint)

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_OPEN] = ReadOnlyState(
                STATE_OPEN,
                R.state_open,
            )
            states[STATE_CLOSED] = ReadOnlyState(
                STATE_CLOSED,
                R.state_closed,
            )
            states[STATE_FORCED_CLOSE] = ControlState(
                STATE_FORCED_CLOSE,
                R.action_force_close,
                R.state_forced_closed
            )
            states[STATE_FORCED_OPEN] = ControlState(
                STATE_FORCED_OPEN,
                R.action_force_open,
                R.state_forced_open
            )
            return states
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val activationTime = extractFieldValue(instance, openingTimeField)
        val name = extractFieldValue(instance, nameField)
        val actuatorPortRaw = extractFieldValue(instance, actuatorPortField)
        val actuatorPort = portFinder.searchForOutputPort(Relay::class.java, actuatorPortRaw)
        val inactiveState = extractFieldValue(instance, inactiveStateField)

        return RadiatorCircuitAutomationUnit(stateChangeReporter, instance, name, states, actuatorPort, activationTime, inactiveState)
    }

    override val iconRaw: String
        get() = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="100px" height="100px" viewBox="0 0 100 100" preserveAspectRatio="xMinYMin meet" enable-background="new 0 0 100 100" xml:space="preserve">
                <path d="M27.39,73.89c0,2.702-2.19,4.893-4.891,4.893l0,0c-2.702,0-4.892-2.19-4.892-4.893V29.112c0-2.702,2.19-4.892,4.892-4.892  l0,0c2.701,0,4.891,2.19,4.891,4.892V73.89z"/>
                <path d="M41.123,73.89c0,2.702-2.19,4.893-4.892,4.893l0,0c-2.702,0-4.892-2.19-4.892-4.893V29.112c0-2.702,2.189-4.892,4.892-4.892  l0,0c2.702,0,4.892,2.19,4.892,4.892V73.89z"/>
                <path d="M54.902,73.89c0,2.702-2.191,4.893-4.892,4.893l0,0c-2.702,0-4.892-2.19-4.892-4.893V29.112c0-2.702,2.19-4.892,4.892-4.892  l0,0c2.7,0,4.892,2.19,4.892,4.892V73.89z"/>
                <path d="M68.611,73.89c0,2.702-2.189,4.893-4.891,4.893l0,0c-2.703,0-4.893-2.19-4.893-4.893V29.112  c0-2.702,2.189-4.892,4.893-4.892l0,0c2.701,0,4.891,2.19,4.891,4.892V73.89z"/>
                <path d="M82.393,73.89c0,2.702-2.189,4.893-4.891,4.893l0,0c-2.703,0-4.892-2.19-4.892-4.893V29.112  c0-2.702,2.188-4.892,4.892-4.892l0,0c2.701,0,4.891,2.19,4.891,4.892V73.89z"/>
                <path d="M7.01,68.557h2.405c0.068,0.536,0.527,0.95,1.082,0.95h1.584c0.555,0,1.013-0.414,1.082-0.95h3.216v2.078  c0,0.322,0.26,0.583,0.583,0.585h0.951c0.037,0,0.073-0.016,0.099-0.041c0.026-0.026,0.041-0.062,0.041-0.099v-7.607  c0-0.036-0.015-0.073-0.041-0.099c-0.026-0.026-0.062-0.042-0.099-0.042h-0.951c-0.323,0-0.583,0.264-0.583,0.585v2.078h-3.216  c-0.062-0.491-0.452-0.879-0.941-0.942v-2.264h2.396c0.498,0,0.9-0.405,0.9-0.901c0-0.497-0.403-0.9-0.9-0.9H7.961  c-0.497,0-0.901,0.403-0.901,0.9c0,0.498,0.404,0.901,0.901,0.901h2.396v2.265c-0.49,0.062-0.879,0.452-0.941,0.941H7.01V68.557z"/>
                <rect x="14.616" y="32.932" width="70.327" height="5.021"/>
                <rect x="17.017" y="64.534" width="75.854" height="5.021"/>
                <rect x="84.058" y="62.982" width="7.122" height="8.418"/>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_ACTUATOR_PORT = "actuatorPortId"
        const val FIELD_OPENING_TIME = "openingTime"
        const val FIELD_INACTIVE_STATE = "inactivityState"
        const val FIELD_THERMOMETER_ID = "thermometerId"
        const val FIELD_TEMPERATURE_CONTROLLER_ID = "temperatureControllerId"
        const val STATE_OPEN = "open"
        const val STATE_CLOSED = "closed"
        const val STATE_FORCED_CLOSE = "forced_close"
        const val STATE_FORCED_OPEN = "forced_open"
    }
}