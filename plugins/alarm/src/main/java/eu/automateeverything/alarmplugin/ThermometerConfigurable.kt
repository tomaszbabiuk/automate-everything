package eu.automateeverything.alarmplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.ConfigurableWithFields
import eu.automateeverything.domain.configurable.NameDescriptionConfigurable
import org.pf4j.Extension

@Extension
class ThermometerConfigurable() : NameDescriptionConfigurable(), ConfigurableWithFields {

    override val hasAutomation = true
    override val editableIcon = false
    override val taggable = false

    override val parent: Class<out Configurable>? = null

    override val addNewRes: Resource
        get() = R.plugin_name

    override val editRes: Resource
        get() = R.plugin_description

    override val titleRes: Resource
        get() = R.plugin_name

    override val descriptionRes: Resource
        get() = R.plugin_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
              <g>
                <title>Layer 1</title>
                <path fill="black" id="svg_1" d="m50.00213,95.867c-13.80742,0 -25.00213,-9.1546 -25.00213,-20.44582c0,-5.17535 2.37519,-9.88811 6.25053,-13.49168l0,-42.72413c0,-8.46712 8.39447,-15.33436 18.7516,-15.33436c10.35088,0 18.74535,6.86724 18.74535,15.33436l0,42.72413c3.88158,3.60357 6.25053,8.31633 6.25053,13.49168c0,11.29122 -11.18846,20.44582 -24.99587,20.44582zm6.25053,-29.2503l0,-10.96664l0,-26.22433l0,-10.22036c0,-2.82152 -2.80024,-5.11146 -6.25053,-5.11146c-3.45342,0 -6.25053,2.28994 -6.25053,5.11146l0,10.22036l0,26.22433l0,10.96664c-3.7222,1.77112 -6.25053,5.03221 -6.25053,8.80447c0,5.64561 5.59735,10.22292 12.50106,10.22292s12.50106,-4.57731 12.50106,-10.22292c0,-3.77226 -2.5346,-7.03336 -6.25053,-8.80447zm-6.25053,16.47167c-5.17544,0 -9.3758,-3.43491 -9.3758,-7.6672c0,-3.33522 2.61897,-6.13885 6.25053,-7.19693l0,-26.18344l6.25053,0l0,26.18344c3.63155,1.05807 6.25053,3.86427 6.25053,7.19693c0,4.23229 -4.20036,7.6672 -9.37579,7.6672z" clip-rule="evenodd" fill-rule="evenodd"/>
              </g>
            </svg>
        """.trimIndent()
}