package eu.automateeverything.centralheatingplugin

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
import eu.automateeverything.sensorsandcontrollersplugin.ThermometerConfigurable
import org.pf4j.Extension

@Extension
class HeatingManifoldConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {

    override val hasAutomation = false
    override val editableIcon = false
    override val taggable = true

    override val parent: Class<out Configurable> = CentralHeatingConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_heating_manifold_add

    override val editRes: Resource
        get() = R.configurable_heating_manifold_edit

    override val titleRes: Resource
        get() = R.configurable_heating_manifolds_title

    override val descriptionRes: Resource
        get() = R.configurable_heating_manifolds_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_TRANSFORMER_PORT] = transformerPortField
            result[FIELD_PUMP_PORT] = pumpPortField
            result[FIELD_MINIMUM_PUMP_WORKING_TIME] = minimumWorkingTimeField
            result[FIELD_CIRCUITS] = circuitIdsField
            return result
        }

    private val minimumWorkingTimeField = DurationField(FIELD_MINIMUM_PUMP_WORKING_TIME, R.field_minimum_pump_working_time_hint,
        Duration(20)
    )

    private val circuitIdsField = InstanceReferenceField(
        FIELD_CIRCUITS, R.field_circuits_hint,
        InstanceReference(ThermometerConfigurable::class.java, InstanceReferenceType.Multiple),
        RequiredStringValidator()
    )

    private val pumpPortField = RelayOutputPortField(FIELD_PUMP_PORT, R.field_pump_port_hint)

    private val transformerPortField = RelayOutputPortField(FIELD_TRANSFORMER_PORT, R.field_transformer_port_hint)

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_HEATING] = ReadOnlyState(
                STATE_HEATING,
                R.state_heating,
            )
            states[STATE_REGULATION] = ReadOnlyState(
                STATE_REGULATION,
                R.state_regulation,
            )
            states[STATE_STANDBY] = ReadOnlyState(
                STATE_STANDBY,
                R.state_standby
            )
            states[STATE_OFF] = ReadOnlyState(
                STATE_OFF,
                R.state_off,
            )
            return states
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = extractFieldValue(instance, nameField)
        val pumpPortRaw = extractFieldValue(instance, pumpPortField)
        val pumpPort =
            if (pumpPortRaw != "") {
                portFinder.searchForOutputPort(Relay::class.java, pumpPortRaw)
            } else {
                null
            }

        val transformerPortRaw = extractFieldValue(instance, transformerPortField)
        val transformerPort =
            if (transformerPortRaw != "") {
                portFinder.searchForOutputPort(Relay::class.java, transformerPortRaw)
            } else {
                null
            }

        val circuitIdsRaw = extractFieldValue(instance, circuitIdsField)
        val circuitIds = circuitIdsRaw.split(",").map { it.toLong() }

        val minPumpWorkingTime = extractFieldValue(instance, minimumWorkingTimeField)

        return HeatingManifoldAutomationUnit(stateChangeReporter, instance, name, states, pumpPort, minPumpWorkingTime,
            transformerPort, circuitIds)
    }

    override val iconRaw: String
        get() = """
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="100px" height="100px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
                    <g>
                        <circle cx="50.159" cy="48.501" r="28.063"/>
                    </g>
                    <polygon points="11.728,41.943 11.728,55.058 5.16,48.523 "/>
                    <polygon points="88.591,55.06 88.591,41.945 95.159,48.48 "/>
                    <polygon points="18.347,71.039 27.62,80.312 18.354,80.336 "/>
                    <polygon points="81.972,25.964 72.698,16.69 81.965,16.667 "/>
                    <polygon points="72.697,80.313 81.971,71.04 81.994,80.306 "/>
                    <polygon points="27.622,16.688 18.349,25.962 18.324,16.695 "/>
                    <polygon points="56.718,10.07 43.604,10.07 50.138,3.501 "/>
                    <polygon points="43.601,86.933 56.716,86.933 50.181,93.502 "/>
                </svg>
        """.trimIndent()

    companion object {
        const val FIELD_PUMP_PORT = "pumpPortId"
        const val FIELD_TRANSFORMER_PORT = "transformerPortId"
        const val FIELD_MINIMUM_PUMP_WORKING_TIME = "minWorkingTime"
        const val FIELD_CIRCUITS = "circuitIds"
        const val STATE_REGULATION = "regulation"
        const val STATE_HEATING = "heating"
        const val STATE_STANDBY = "standby"
        const val STATE_OFF = "off"
    }
}