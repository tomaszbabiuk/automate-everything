package eu.automateeverything.coreplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.devices.DevicesConfigurable
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.PowerLevel
import org.pf4j.Extension
import java.math.BigDecimal

@Extension
class PowerRegulatorConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter) : ControllerConfigurable<PowerLevel>(PowerLevel::class.java) {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            result[FIELD_READ_ONLY] = readOnlyField
            return result
        }

    override val parent: Class<out Configurable?>
        get() = DevicesConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_power_regulator_add

    override val editRes: Resource
        get() = R.configurable_power_regulator_edit

    override val titleRes: Resource
        get() = R.configurable_power_regulators_title

    override val descriptionRes: Resource
        get() = R.configurable_power_regulators_description

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

    override val hasAutomation: Boolean = true
    override val editableIcon: Boolean = true
    override val taggable: Boolean = true

    private val portField = PowerLevelOutputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())
    private val readOnlyField = BooleanField(FIELD_READ_ONLY, R.field_readonly_hint, 0, false)

    companion object {
        const val FIELD_PORT = "portId"
        const val FIELD_READ_ONLY = "readOnly"
    }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<PowerLevel> {
        val portId = extractFieldValue(instance, portField)
        val port = portFinder.searchForOutputPort(PowerLevel::class.java, portId)
        val name = instance.fields[FIELD_NAME]!!
        val readOnly = extractFieldValue(instance, readOnlyField)
        return PowerRegulatorAutomationUnit(name, instance, port, readOnly, stateChangeReporter)
    }

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.PowerLevel

    override fun extractMinValue(instance: InstanceDto): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun extractMaxValue(instance: InstanceDto): BigDecimal {
        return 100.0.toBigDecimal()
    }
}