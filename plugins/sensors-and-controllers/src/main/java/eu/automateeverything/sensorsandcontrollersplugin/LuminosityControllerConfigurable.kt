/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.ControllerAutomationUnitBase
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.Luminosity
import org.pf4j.Extension
import java.math.BigDecimal

@Extension
class LuminosityControllerConfigurable(
    private val eventsSink: EventsSink
) : ControllerConfigurable<Luminosity>(Luminosity::class.java) {

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
        get() = R.configurable_luminosity_controller_add

    override val editRes: Resource
        get() = R.configurable_luminosity_controller_edit

    override val titleRes: Resource
        get() = R.configurable_luminosity_controller_title

    override val descriptionRes: Resource
        get() = R.configurable_luminosity_controller_description

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
                    <title>Layer 2</title>
                    <g id="svg_1" fill-rule="evenodd" fill="none">
                        <path id="svg_2" fill="#000000" d="m61.18,36.99647c0,-5.52195 -4.78161,-9.9984 -10.68,-9.9984c-5.89839,0 -10.68,4.47644 -10.68,9.9984c0,5.52195 4.7816,9.99839 10.68,9.99839c5.89839,0 10.68,-4.47644 10.68,-9.99839zm-19.224,0c0,-4.41757 3.82528,-7.99871 8.54399,-7.99871c4.71872,0 8.54399,3.58115 8.54399,7.99871c0,4.41757 -3.82528,7.99871 -8.54399,7.99871c-4.71872,0 -8.54399,-3.58114 -8.54399,-7.99871zm8.54399,-21.99647c-0.58983,0 -1.068,0.42961 -1.068,0.99524l0,8.00791c0,0.54965 0.4954,0.99524 1.068,0.99524c0.58983,0 1.068,-0.42961 1.068,-0.99524l0,-8.00791c0,-0.54965 -0.49541,-0.99524 -1.068,-0.99524zm16.61951,6.44468c-0.41708,-0.39046 -1.07969,-0.40321 -1.50691,-0.00326l-6.04847,5.66247c-0.41515,0.38866 -0.4014,1.03167 0.00348,1.41072c0.41708,0.39048 1.07969,0.40321 1.5069,0.00326l6.04846,-5.66245c0.41516,-0.38866 0.40141,-1.0317 -0.00346,-1.41074l0,0.00001zm6.88403,15.55884c0,-0.55219 -0.4589,-0.99984 -1.06309,-0.99984l-8.55382,0c-0.58712,0 -1.06309,0.4638 -1.06309,0.99984c0,0.55222 0.45892,0.99984 1.06309,0.99984l8.55382,0c0.58715,0 1.06309,-0.46378 1.06309,-0.99984zm-6.88403,15.55886c0.41706,-0.39046 0.43069,-1.01078 0.00346,-1.41074l-6.04846,-5.66245c-0.41516,-0.38868 -1.10203,-0.3758 -1.5069,0.00323c-0.41707,0.39048 -0.43071,1.01078 -0.00348,1.41074l6.04847,5.66247c0.41518,0.38866 1.10203,0.37577 1.50691,-0.00326zm-16.61951,6.44469c0.58983,0 1.068,-0.42964 1.068,-0.99524l0,-8.00791c0,-0.54965 -0.49541,-0.99524 -1.068,-0.99524c-0.58983,0 -1.068,0.42961 -1.068,0.99524l0,8.00791c0,0.54965 0.4954,0.99524 1.068,0.99524zm-16.61951,-6.44469c0.41708,0.39046 1.07969,0.4032 1.50691,0.00326l6.04847,-5.66247c0.41515,-0.38866 0.4014,-1.03168 -0.00348,-1.41074c-0.41708,-0.39046 -1.07968,-0.40319 -1.5069,-0.00323l-6.04847,5.66245c-0.41515,0.38866 -0.40142,1.0317 0.00348,1.41074l-0.00001,0zm-6.88403,-15.55886c0,0.55222 0.4589,0.99984 1.06306,0.99984l8.55385,0c0.58712,0 1.06308,-0.46378 1.06308,-0.99984c0,-0.55219 -0.45892,-0.99984 -1.06308,-0.99984l-8.55385,0c-0.58712,0 -1.06306,0.4638 -1.06306,0.99984zm6.88403,-15.55884c-0.4171,0.39046 -0.4307,1.01078 -0.00348,1.41074l6.04847,5.66245c0.41515,0.38868 1.10203,0.3758 1.50691,-0.00326c0.41707,-0.39046 0.4307,-1.01078 0.00348,-1.41072l-6.04847,-5.66247c-0.41517,-0.38866 -1.10203,-0.37578 -1.50691,0.00326l0,-0.00001z"/>
                    </g>
                </g>
            </svg>
        """.trimIndent()

    private val minField = NullableBigDecimalField(FIELD_MIN, R.field_min_lum_hint, BigDecimal.ZERO, GreaterThanZeroValidator())
    private val maxField = NullableBigDecimalField(FIELD_MAX, R.field_max_lum_hint, BigDecimal.ZERO, GreaterThanZeroValidator())
    private val defaultField = NullableBigDecimalField(FIELD_DEFAULT, R.field_default_lum_hint, BigDecimal.ZERO, GreaterThanZeroValidator())
    private val automationOnlyField = BooleanField(FIELD_AUTOMATION_ONLY, R.field_automation_only_hint,false)

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<Luminosity> {
        val name = extractFieldValue(instance, nameField)
        val automationOnly = extractFieldValue(instance, automationOnlyField)
        val min = extractFieldValue(instance, minField)
        val max = extractFieldValue(instance, maxField)
        val default = extractFieldValue(instance, defaultField)
        return ControllerAutomationUnitBase(
            Luminosity::class.java, eventsSink, name, instance, automationOnly,
            min.wrapped!!, max.wrapped!!, BigDecimal.ONE, Luminosity(default.wrapped!!))
    }

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Luminosity

    override fun extractMinValue(instance: InstanceDto): BigDecimal {
        return BigDecimal.ZERO
    }

    override fun extractMaxValue(instance: InstanceDto): BigDecimal {
        return 100.0.toBigDecimal()
    }

    companion object {
        const val FIELD_MIN = "min"
        const val FIELD_MAX = "max"
        const val FIELD_DEFAULT = "default"
        const val FIELD_AUTOMATION_ONLY = "automation_only"
    }
}