package eu.geekhome.coreplugin

import eu.geekhome.services.configurable.*
import eu.geekhome.services.localization.Resource
import org.pf4j.Extension
import java.util.HashMap

@Extension
class HygrometerConfigurable : SensorConfigurable() {

    override val parent: Class<out Configurable?>
        get() = MetersConfigurable::class.java

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap(super.fieldDefinitions)
            result[OnOffDeviceConfigurable.FIELD_PORT] = portField
            return result
        }

    override val addNewRes: Resource
        get() = R.configurable_hygrometer_add

    override val editRes: Resource
        get() = R.configurable_hygrometer_edit

    override val titleRes: Resource
        get() = R.configurable_hygrometer_title

    override val descriptionRes: Resource
        get() = R.configurable_hygrometer_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
                <g>
                    <title>Layer 1</title>
                    <path fill="black" id="svg_1" d="m86.67137,42.64997l-23.49729,-22.24379l-23.49202,22.24047c-6.29897,5.94919 -9.76639,13.86205 -9.76639,22.2837c0,17.35874 14.92184,31.48516 33.25841,31.48516c18.34008,0 33.26192,-14.12476 33.26192,-31.48516c0,-8.42165 -3.46742,-16.33617 -9.76463,-22.28037zm-23.49729,44.08687c-12.70157,0 -23.03532,-9.78175 -23.03532,-21.8065c0,-5.8328 2.40295,-11.31643 6.76796,-15.43996l16.26736,-15.3984l16.27438,15.40172c4.36326,4.12021 6.76621,9.60384 6.76621,15.43664c0,12.02142 -10.33726,21.8065 -23.04059,21.8065zm-33.66768,-72.98817l-10.73775,-10.16417l-10.736,10.16251c-2.88073,2.72186 -4.46865,6.33993 -4.46865,10.18745c0,7.93281 6.82066,14.38913 15.20465,14.38913s15.20465,-6.45632 15.20465,-14.38913c0.00176,-3.84752 -1.58616,-7.46392 -4.46689,-10.18579zm-10.736,20.9502c-6.26911,0 -11.37187,-4.82686 -11.37187,-10.76109c0,-2.87816 1.18567,-5.58339 3.34446,-7.62188l8.02741,-7.59694l8.02741,7.59694c2.15704,2.03849 3.34446,4.74372 3.34446,7.62022c0,5.93423 -5.10276,10.76275 -11.37187,10.76275z"/>
                </g>
            </svg>
        """.trimIndent()

    private val portField = HumidityReadPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())

    companion object {
        const val FIELD_PORT = "portId"
    }
}