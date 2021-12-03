package eu.automateeverything.coreplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.Temperature
import org.pf4j.Extension

@Extension
class TemperatureControllerConfigurable(
    private val stateChangeReporter: StateChangeReporter
) : DeviceConfigurableWithBlockCategory<Temperature>(Temperature::class.java) {

    override val parent: Class<out Configurable>? = null

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_MIN_TEMP] = minField
            result[FIELD_MAX_TEMP] = maxField
            result[FIELD_DEFAULT_TEMP] = defaultField
            result[FIELD_READ_ONLY] = readOnlyField
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
            <?xml version="1.0" encoding="utf-8"?>
            <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
            <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
            	 width="100px" height="100px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
            <g>
            	<path d="M44.444,61.225v-5.669H37.5v-2.778h6.944v-5.555H37.5v-2.778h6.944v-5.556H37.5v-2.777h6.944v-5.556H37.5v-2.778h6.944
            		v-4.167c0-3.064-2.492-5.556-5.556-5.556s-5.555,2.492-5.555,5.556v37.613c-2.935,1.704-5.019,4.707-5.459,8.22h22.03
            		C49.463,65.932,47.379,62.929,44.444,61.225z"/>
            	<path d="M50,0C22.386,0,0,22.386,0,50c0,27.613,22.386,50,50,50c27.613,0,50-22.387,50-50C100,22.386,77.613,0,50,0z M75,31.944
            		l13.889,13.889H61.111L75,31.944z M38.889,87.5c-9.19,0-16.667-7.477-16.667-16.667c0-4.919,2.155-9.334,5.556-12.387V23.611
            		c0-6.127,4.984-11.111,11.111-11.111C45.016,12.5,50,17.484,50,23.611v34.835c3.4,3.053,5.556,7.468,5.556,12.387
            		C55.556,80.023,48.08,87.5,38.889,87.5z M75,68.056L61.111,54.167h27.777L75,68.056z"/>
            </g>
            </svg>
        """.trimIndent()

    private val minField = TemperatureField(FIELD_MIN_TEMP, R.field_min_temp_hint, 10, (273.15 + 10.0).toBigDecimal(), RequiredBigDecimalValidator())
    private val maxField = TemperatureField(FIELD_MAX_TEMP, R.field_max_temp_hint, 10, (273.15 + 20.0).toBigDecimal(), RequiredBigDecimalValidator())
    private val defaultField = TemperatureField(FIELD_DEFAULT_TEMP, R.field_default_temp_hint, 10, (273.15 -30.0).toBigDecimal(), RequiredBigDecimalValidator())
    private val readOnlyField = BooleanField(FIELD_READ_ONLY, R.field_readonly_hint, 0, false)

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<Temperature> {
        val name = instance.fields[FIELD_NAME]!!
        val readOnly = extractFieldValue(instance, readOnlyField)
        val min = extractFieldValue(instance, minField)
        val max = extractFieldValue(instance, maxField)
        val default = extractFieldValue(instance, defaultField)
        return TemperatureControllerAutomationUnit(name, min.wrapped!!, max.wrapped!!, default.wrapped!!, instance, readOnly, stateChangeReporter)
    }

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Temperature

    override val hasAutomation: Boolean
        get() = true

    companion object {
        const val FIELD_MIN_TEMP = "min_t"
        const val FIELD_MAX_TEMP = "max_t"
        const val FIELD_DEFAULT_TEMP = "default_t"
        const val FIELD_READ_ONLY = "read_only"
    }
}