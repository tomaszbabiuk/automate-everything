package eu.automateeverything.coreplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.EvaluableAutomationUnitBase
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.conditions.ConditionsConfigurable
import org.pf4j.Extension
import java.util.*

@Extension
class TwilightConditionConfigurable : ConditionConfigurable() {

    override val parent: Class<out Configurable?>
        get() = ConditionsConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_twilightcondition_add

    override val editRes: Resource
        get() = R.configurable_twilightcondition_edit

    override val titleRes: Resource
        get() = R.configurable_twilightcondition_title

    override val descriptionRes: Resource
        get() = R.configurable_twilightcondition_description

    override val iconRaw: String
        get() = """<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
                     <g>
                      <title>Layer 1</title>
                      <path fill="black" d="m61,100c-27.57,0 -50,-22.43 -50,-50s22.43,-50 50,-50c4.80099,0 9.575,0.691 14.188,2.054l12.019,3.552l-12.019,3.552c-17.96,5.307 -30.50401,22.102 -30.50401,40.842c0,18.739 12.544,35.533 30.503,40.84l12.01501,3.549l-12.014,3.555c-4.614,1.364 -9.38701,2.056 -14.188,2.056l0,0zm0,-92.593c-23.486,0 -42.592,19.108 -42.592,42.593s19.106,42.59299 42.592,42.59299c0.043,0 0.086,0 0.13,0c-14.488,-8.958 -23.853,-25.04999 -23.853,-42.59299c0,-17.542 9.365,-33.633 23.851,-42.593c-0.044,0 -0.086,0 -0.128,0l0,0z" id="svg_1"/>
                     </g>
                    </svg>"""

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> =
                HashMap<String, FieldDefinition<*>>(super.fieldDefinitions)
            result[FIELD_LONGITUDE] = longitudeField
            result[FIELD_LATITUDE] = latitudeField
            return result
        }

    private val longitudeField = DoubleField(FIELD_LONGITUDE, R.field_longitude_hint, 0, 0.0, RequiredDoubleValidator())

    private val latitudeField = DoubleField(FIELD_LATITUDE, R.field_latitude_hint, 0, 0.0, RequiredDoubleValidator())

    override fun buildEvaluator(instance: InstanceDto): EvaluableAutomationUnitBase {
        val longitude = extractFieldValue(instance, longitudeField)
        val latitude = extractFieldValue(instance, latitudeField)
        return TwilightConditionAutomationUnit(longitude, latitude)
    }

    companion object {
        const val FIELD_LONGITUDE = "longitude"
        const val FIELD_LATITUDE = "latitide"
    }
}