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

package eu.automateeverything.scenesplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.configurable.BooleanField
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.domain.configurable.StateDeviceConfigurable
import eu.automateeverything.domain.events.EventBus
import org.pf4j.Extension
import java.util.HashMap

@Extension
class SceneConfigurable(
    private val eventBus: EventBus
) : StateDeviceConfigurable() {
    override val parent: Class<out Configurable>?
        get() = null

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_AUTOMATION_ONLY] = automationOnlyField
            return result
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = extractFieldValue(instance, nameField)
        val automationOnly = extractFieldValue(instance, automationOnlyField)
        return SceneAutomationUnit(eventBus, instance, name, automationOnly, states)
    }

    private val automationOnlyField = BooleanField(FIELD_AUTOMATION_ONLY, R.field_automation_only_hint, false)


    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = State.buildReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_ACTIVE] = State.buildControlState(
                STATE_ACTIVE,
                R.state_active,
                R.action_activate,
                isSignaled = true
            )
            states[STATE_INACTIVE] = State.buildControlState(
                STATE_INACTIVE,
                R.state_inactive,
                R.action_deactivate,
            )
            return states
        }

    override val addNewRes: Resource
        get() = R.configurable_scene_add

    override val editRes: Resource
        get() = R.configurable_scene_edit

    override val titleRes: Resource
        get() = R.configurable_scene_title

    override val descriptionRes: Resource
        get() = R.configurable_scene_description

    override val iconRaw: String
        get() = """<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
                     <g class="layer">
                      <title>Created by hafiudin</title>
                      <g id="svg_1" transform="translate(0,-952.36218)">
                       <path d="m83.62422,957.37689c-25.19636,6.60594 -51.097,12.46295 -72.68677,18.18453c-0.27981,0.0618 -1.5212,0.45068 -2.53122,1.28104c-1.28258,1.05443 -2.35319,3.04933 -2.40623,5.43661c-0.0085,0.34929 0.04464,0.69999 0.15625,1.03108l3.84371,11.52937l0,46.52368c-0.00005,0.01 -0.00005,0.021 0,0.031c0.02559,1.958 0.99966,3.789 2.28123,4.7492c1.28156,0.9604 2.59851,1.2186 3.71871,1.2186l71.99928,0c1.2088,0 2.49608,-0.3218 3.74996,-1.2811c1.25389,-0.9593 2.24998,-2.7586 2.24998,-4.7179l0,-46.99236c-0.00016,-1.57049 -1.42923,-2.99935 -2.99997,-2.99951l-55.65569,0c18.60152,-4.85762 37.16743,-10.18206 55.43694,-15.12253c1.47149,-0.40669 2.46623,-2.0741 2.12498,-3.56191c-0.95121,-4.04022 -2.12777,-8.07044 -3.15622,-11.52937c-0.62609,-1.43585 -1.73398,-2.58683 -2.90622,-3.15573c-1.17224,-0.56891 -2.18537,-0.59366 -2.62497,-0.6249c-0.19727,-0.0196 -0.39648,-0.0196 -0.59375,0l0,0.0002zm0.5625,6.03026c0.70166,2.40032 1.40195,5.06809 2.12498,7.84247l-5.6562,1.531c-1.8098,-1.80962 -4.02046,-4.09533 -6.74993,-6.81139l10.28115,-2.56208zm-17.12483,4.28055c2.51328,2.43566 4.84728,4.84513 6.90618,6.93637l-12.15613,3.31196c-1.689,-1.64803 -4.17265,-4.23028 -7.09368,-7.15508l12.34363,-3.09325zm-19.18731,4.81172c2.6779,2.62091 5.17577,5.14829 7.24993,7.24881l-12.15613,3.31196c-1.86805,-1.85628 -4.4873,-4.5217 -7.46867,-7.49877l12.37487,-3.062zm-19.21856,4.78047c2.68896,2.62553 5.27896,5.24848 7.62493,7.62375l-9.03116,2.46835c-1.85428,-1.83292 -4.63571,-4.70151 -7.74992,-7.81123l9.15615,-2.28087zm-15.93734,4.03059c2.79348,2.72909 5.4568,5.4593 7.84367,7.87371l-5.62494,1.531l-2.87497,-8.62359c-0.02532,-0.2082 0.01879,-0.53361 0.15625,-0.59365c0.03606,-0.0296 0.30293,-0.12229 0.49999,-0.18747zm3.28122,16.05987l71.99928,0l0,43.99285l-71.99928,0c0.0017,-0.032 0.00088,-0.0005 0,-0.062l0,-43.93085zm10.68739,7.99865c-1.57096,0.082 -2.92606,1.5851 -2.84424,3.1558c0.08182,1.5707 1.5858,2.9253 3.15674,2.8433l49.9995,0c1.58492,0.022 3.04269,-1.4147 3.04269,-2.9995c0,-1.5849 -1.45777,-3.022 -3.04269,-2.9996c-16.7626,0 -33.57208,0 -50.312,0zm0,10.9982c-1.57096,0.082 -2.92606,1.5851 -2.84424,3.1558c0.08182,1.5707 1.5858,2.9253 3.15674,2.8433l49.9995,0c1.58492,0.022 3.04269,-1.4147 3.04269,-2.9995c0,-1.5849 -1.45777,-3.022 -3.04269,-2.9996c-16.7626,0 -33.57208,0 -50.312,0z" fill="#000000" id="svg_2"/>
                      </g>
                     </g>
                    </svg>"""

    companion object {
        const val STATE_ACTIVE = "active"
        const val STATE_INACTIVE = "inactive"
        const val FIELD_AUTOMATION_ONLY = "automation_only"
    }
}
