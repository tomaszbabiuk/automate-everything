package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import eu.automateeverything.domain.automation.ControllerAutomationUnitBase
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.Temperature
import org.pf4j.Extension
import java.math.BigDecimal

@Extension
class TemperatureControllerConfigurable(
    private val stateChangeReporter: StateChangeReporter
) : ControllerConfigurable<Temperature>(Temperature::class.java) {

    override val parent: Class<out Configurable> = ControllersConfigurable::class.java

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_MIN] = minField
            result[FIELD_MAX] = maxField
            result[FIELD_DEFAULT_TEMP] = defaultField
            result[FIELD_AUTOMATION_ONLY] = automationOnlyField
            return result
        }

    override val addNewRes: Resource
        get() = R.configurable_temperature_controller_add

    override val editRes: Resource
        get() = R.configurable_temperature_controller_edit

    override val titleRes: Resource
        get() = R.configurable_temperature_controller_title

    override val descriptionRes: Resource
        get() = R.configurable_temperature_controller_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
             <metadata/>
             <g>
              <title>Layer 1</title>
              <path d="m50.497,1.724c-17.727,0 -40.22,11.276 -37.021,65.276l74.075,0c3.069,-51 -19.372,-65.276 -37.054,-65.276zm27.886,58.276l-56.28,0c-2.43,-40 14.659,-49.596 28.12701,-49.596c13.43499,0 30.48599,10.596 28.15299,49.596z" id="svg_1"/>
              <rect x="60" y="63" width="10" height="35" id="svg_2"/>
              <rect x="32" y="63" width="10" height="35" id="svg_3"/>
             </g>
             <g>
              <title>Layer 1</title>
              <path fill="black" id="svg_1" d="m50.00037,57.00986c-6.07495,0 -11.00037,-4.19328 -11.00037,-9.36523c0,-2.37058 1.04503,-4.52926 2.7501,-6.17989l0,-19.56982c0,-3.87838 3.69337,-7.02393 8.25028,-7.02393c4.55416,0 8.24753,3.14555 8.24753,7.02393l0,19.56982c1.70781,1.65062 2.7501,3.80931 2.7501,6.17989c0,5.17195 -4.92267,9.36523 -10.99763,9.36523zm2.7501,-13.39814l0,-5.02328l0,-12.01208l0,-4.68144c0,-1.2924 -1.23204,-2.34131 -2.7501,-2.34131c-1.51942,0 -2.75009,1.04891 -2.75009,2.34131l0,4.68144l0,12.01208l0,5.02328c-1.63768,0.81126 -2.7501,2.30502 -2.7501,4.03291c0,2.58597 2.46271,4.68261 5.50019,4.68261s5.50019,-2.09664 5.50019,-4.68261c0,-1.72789 -1.11516,-3.22165 -2.75009,-4.03291zm-2.7501,7.54486c-2.27707,0 -4.12514,-1.57336 -4.12514,-3.51196c0,-1.52771 1.15229,-2.81191 2.7501,-3.29656l0,-11.99335l2.7501,0l0,11.99335c1.59781,0.48465 2.75009,1.77003 2.75009,3.29656c0,1.9386 -1.84806,3.51196 -4.12514,3.51196z" clip-rule="evenodd" fill-rule="evenodd"/>
             </g>
            </svg>
        """.trimIndent()

    private val minField = TemperatureField(FIELD_MIN, R.field_min_temp_hint, 10, (273.15).toBigDecimal(), RequiredBigDecimalValidator())
    private val maxField = TemperatureField(FIELD_MAX, R.field_max_temp_hint, 10, (273.15).toBigDecimal(), RequiredBigDecimalValidator())
    private val defaultField = TemperatureField(FIELD_DEFAULT_TEMP, R.field_default_temp_hint, 10, (273.15).toBigDecimal(), RequiredBigDecimalValidator())
    private val automationOnlyField = BooleanField(FIELD_AUTOMATION_ONLY, R.field_automation_only_hint, 0, false)

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<Temperature> {
        val name = instance.fields[FIELD_NAME]!!
        val automationOnly = extractFieldValue(instance, automationOnlyField)
        val min = extractFieldValue(instance, minField)
        val max = extractFieldValue(instance, maxField)
        val default = extractFieldValue(instance, defaultField)
        return ControllerAutomationUnitBase(Temperature::class.java, name, instance, automationOnly,
            min.wrapped!!, max.wrapped!!, 0.1.toBigDecimal(), Temperature(default.wrapped!!), stateChangeReporter)
    }

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Temperature

    override val hasAutomation: Boolean
        get() = true

    companion object {
        const val FIELD_MIN = "min"
        const val FIELD_MAX = "max"
        const val FIELD_DEFAULT_TEMP = "default"
        const val FIELD_AUTOMATION_ONLY = "automation_only"
    }

    override fun extractMinValue(instance: InstanceDto): BigDecimal {
        return extractFieldValue(instance, minField).wrapped!!
    }

    override fun extractMaxValue(instance: InstanceDto): BigDecimal {
        return extractFieldValue(instance, maxField).wrapped!!
    }
}