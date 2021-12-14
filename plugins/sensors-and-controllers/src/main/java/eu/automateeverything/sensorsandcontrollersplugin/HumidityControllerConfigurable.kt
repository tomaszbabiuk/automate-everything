package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.ControllerAutomationUnitBase
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.Humidity
import org.pf4j.Extension
import java.math.BigDecimal

@Extension
class HumidityControllerConfigurable(
    private val stateChangeReporter: StateChangeReporter
) : ControllerConfigurable<Humidity>(Humidity::class.java) {

    override val parent: Class<out Configurable> = ControllersConfigurable::class.java

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_MIN] = minField
            result[FIELD_MAX] = maxField
            result[FIELD_DEFAULT] = defaultField
            result[FIELD_AUTOMATION_ONLY] = automationOnlyField
            return result
        }

    override val addNewRes: Resource
        get() = R.configurable_humidity_controller_add

    override val editRes: Resource
        get() = R.configurable_humidity_controller_edit

    override val titleRes: Resource
        get() = R.configurable_humidity_controller_title

    override val descriptionRes: Resource
        get() = R.configurable_humidity_controller_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
                <metadata/>
                <g>
                    <title>Layer 1</title>
                    <path d="m50.497,1.724c-17.727,0 -40.22,11.276 -37.021,65.276l74.075,0c3.069,-51 -19.372,-65.276 -37.054,-65.276zm27.886,58.276l-56.28,0c-2.43,-40 14.659,-49.596 28.12701,-49.596c13.43499,0 30.48599,10.596 28.15299,49.596z" id="svg_1"/>
                    <rect x="60" y="63" width="10" height="35" id="svg_2"/>
                    <rect x="32" y="63" width="10" height="35" id="svg_3"/>
                </g>
                <g>
                    <title>Layer 1</title>
                    <path fill="black" id="svg_1" d="m63.74825,33.8997l-9.32888,-8.34606l-9.32679,8.34481c-2.50081,2.23219 -3.87745,5.20116 -3.87745,8.36104c0,6.51315 5.92426,11.8135 13.20424,11.8135c7.28137,0 13.20563,-5.29973 13.20563,-11.8135c0,-3.15988 -1.37663,-6.12947 -3.87675,-8.35979zm-9.32888,16.54177c-5.04277,0 -9.14547,-3.67019 -9.14547,-8.18198c0,-2.18852 0.95402,-4.24603 2.68701,-5.79321l6.45846,-5.77761l6.46125,5.77886c1.7323,1.54594 2.68632,3.60344 2.68632,5.79196c0,4.51054 -4.1041,8.18198 -9.14757,8.18198zm-13.36673,-27.38579l-4.2631,-3.81368l-4.2624,3.81306c-1.14371,1.02127 -1.77414,2.3788 -1.77414,3.82242c0,2.97646 2.70794,5.39893 6.03654,5.39893s6.03654,-2.42247 6.03654,-5.39893c0.0007,-1.44362 -0.62974,-2.80053 -1.77344,-3.8218zm-4.2624,7.8607c-2.48896,0 -4.51485,-1.81108 -4.51485,-4.03765c0,-1.07991 0.47073,-2.09494 1.32782,-2.8598l3.18703,-2.85044l3.18703,2.85044c0.85639,0.76486 1.32782,1.77989 1.32782,2.85917c0,2.22657 -2.02589,4.03828 -4.51485,4.03828z"/>
                </g>
            </svg>
        """.trimIndent()

    private val minField = PercentField(FIELD_MIN, R.field_min_hum_hint, BigDecimal.ZERO)
    private val maxField = PercentField(FIELD_MAX, R.field_max_hum_hint, BigDecimal.ZERO)
    private val defaultField = PercentField(FIELD_DEFAULT, R.field_default_hum_hint, BigDecimal.ZERO)
    private val automationOnlyField = BooleanField(FIELD_AUTOMATION_ONLY, R.field_automation_only_hint, 0, false)

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<Humidity> {
        val name = extractFieldValue(instance, nameField)
        val automationOnly = extractFieldValue(instance, automationOnlyField)
        val min = extractFieldValue(instance, minField)
        val max = extractFieldValue(instance, maxField)
        val default = extractFieldValue(instance, defaultField)
        return ControllerAutomationUnitBase(Humidity::class.java, stateChangeReporter, name, instance, automationOnly,
            min, max, BigDecimal.ONE, Humidity(default))
    }

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Humidity

    override val hasAutomation: Boolean
        get() = true

    companion object {
        const val FIELD_MIN = "min"
        const val FIELD_MAX = "max"
        const val FIELD_DEFAULT = "default"
        const val FIELD_AUTOMATION_ONLY = "automation_only"
    }

    override fun extractMinValue(instance: InstanceDto): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun extractMaxValue(instance: InstanceDto): BigDecimal {
        return 100.0.toBigDecimal()
    }
}