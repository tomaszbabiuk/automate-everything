/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.automation.ControlState
import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.Relay
import org.pf4j.Extension

@Extension
class ThermalActuatorConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {

    override val hasAutomation = true
    override val editableIcon = true
    override val taggable = true

    override val parent: Class<out Configurable> = CentralHeatingConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_thermal_actuator_add

    override val editRes: Resource
        get() = R.configurable_thermal_actuator_edit

    override val titleRes: Resource
        get() = R.configurable_thermal_actuators_title

    override val descriptionRes: Resource
        get() = R.configurable_thermal_actuators_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_ACTUATOR_PORT] = actuatorPortField
            result[FIELD_OPENING_TIME] = openingTimeField
            result[FIELD_INACTIVE_STATE] = inactiveStateField
            return result
        }

    private val openingTimeField = DurationField(FIELD_OPENING_TIME, R.field_opening_time_hint,
        Duration(120)
    )

    private val actuatorPortField = RelayOutputPortField(FIELD_ACTUATOR_PORT, R.field_actuator_port_hint, RequiredStringValidator())

    private val inactiveStateField = ContactTypeField(FIELD_INACTIVE_STATE, R.field_inactive_state_hint)

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_ENABLED] = ControlState(
                STATE_ENABLED,
                R.state_enabled,
                R.action_enable,
                isSignaled = true
            )
            states[STATE_DISABLED] = ControlState(
                STATE_DISABLED,
                R.state_disabled,
                R.action_disable
            )
            return states
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val activationTime = extractFieldValue(instance, openingTimeField)
        val name = extractFieldValue(instance, nameField)
        val actuatorPortRaw = extractFieldValue(instance, actuatorPortField)
        val actuatorPort = portFinder.searchForOutputPort(Relay::class.java, actuatorPortRaw)
        val inactiveState = extractFieldValue(instance, inactiveStateField)

        return ThermalActuatorAutomationUnit(stateChangeReporter, instance, name, states, actuatorPort, activationTime, inactiveState)
    }

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>valve by Ben Davis from the Noun Project</title>
              <g id="svg_10"/>
              <path d="m841.28764,200.39178l-40.40434,0c-3.70373,0 -6.73406,3.91315 -6.73406,8.69589l0,8.69589l-53.87245,0l0,-60.87121c0,-4.78274 -3.03033,-8.69589 -6.73406,-8.69589l-26.93622,0l0,-26.08766l40.40434,0c3.70373,0 6.73406,-3.91315 6.73406,-8.69589l0,-139.1342c0,-4.78274 -3.03033,-8.69589 -6.73406,-8.69589l-6.73406,0l0,-17.39178c0,-4.78274 -3.03033,-8.69589 -6.73406,-8.69589l-161.61735,0c-3.70373,0 -6.73406,3.91315 -6.73406,8.69589l0,17.39178l-6.73406,0c-3.70373,0 -6.73406,3.91315 -6.73406,8.69589l0,139.1342c0,4.78274 3.03033,8.69589 6.73406,8.69589l40.40434,0l0,26.08766l-26.93622,0c-3.70373,0 -6.73406,3.91315 -6.73406,8.69589l0,60.87121l-67.34056,0l0,-8.69589c0,-4.78274 -3.03033,-8.69589 -6.73406,-8.69589l-40.40434,0c-3.70373,0 -6.73406,3.91315 -6.73406,8.69589l0,208.7013c0,4.78274 3.03033,8.69589 6.73406,8.69589l40.40434,0c3.70373,0 6.73406,-3.91315 6.73406,-8.69589l0,-8.69589l296.29847,0l0,8.69589c0,4.78274 3.03033,8.69589 6.73406,8.69589l40.40434,0c3.70373,0 6.73406,-3.91315 6.73406,-8.69589l0,-208.7013c0,-4.78274 -3.03033,-8.69589 -6.73406,-8.69589l0.00001,0zm-6.73406,104.35065l-26.93622,0l0,-34.78355l26.93622,0l0,34.78355zm-26.93622,17.39178l26.93622,0l0,34.78355l-26.93622,0l0,-34.78355zm0,-104.35065l26.93622,0l0,34.78355l-26.93622,0l0,-34.78355zm-134.68112,-113.04654l0,-121.74243l13.46811,0l0,121.74243l-13.46811,0zm-26.93622,0l0,-121.74243l13.46811,0l0,121.74243l-13.46811,0zm-26.93622,0l0,-121.74243l13.46811,0l0,121.74243l-13.46811,0zm-26.93622,0l0,-121.74243l13.46811,0l0,121.74243l-13.46811,0zm107.7449,-121.74243l13.46811,0l0,121.74243l-13.46811,0l0,-121.74243zm40.40434,121.74243l-13.46811,0l0,-121.74243l13.46811,0l0,121.74243zm-161.61735,-147.83009l148.14923,0l0,8.69589l-148.14923,0l0,-8.69589zm-13.46811,26.08766l13.46811,0l0,121.74243l-13.46811,0l0,-121.74243zm47.13839,139.1342l80.80867,0l0,26.08766l-80.80867,0l0,-26.08766zm-127.94707,182.61364l-26.93622,0l0,-34.78355l26.93622,0l0,34.78355zm-26.93622,17.39178l26.93622,0l0,34.78355l-26.93622,0l0,-34.78355zm0,-104.35065l26.93622,0l0,34.78355l-26.93622,0l0,-34.78355zm26.93622,191.30953l-26.93622,0l0,-34.78355l26.93622,0l0,34.78355zm13.46811,-17.39178l0,-156.52598l74.07462,0c3.70373,0 6.73406,-3.91315 6.73406,-8.69589l0,-60.87121l148.14923,0l0,60.87121c0,4.78274 3.03033,8.69589 6.73406,8.69589l60.60651,0l0,156.52598l-296.29847,0l-0.00001,0zm336.70281,17.39178l-26.93622,0l0,-34.78355l26.93622,0l0,34.78355z" fill="black" id="svg_4" transform="translate(564 345)"/>
              <path d="m86.75432,52.63038l-7.60434,0c-0.69706,0 -1.26739,0.59184 -1.26739,1.31519l0,1.31519l-10.13912,0l0,-9.20633c0,-0.72335 -0.57033,-1.31519 -1.26739,-1.31519l-5.06956,0l0,-3.94557l7.60434,0c0.69706,0 1.26739,-0.59184 1.26739,-1.31519l0,-21.04303c0,-0.72335 -0.57033,-1.31519 -1.26739,-1.31519l-1.26739,0l0,-2.63038c0,-0.72335 -0.57033,-1.31519 -1.26739,-1.31519l-30.41736,0c-0.69706,0 -1.26739,0.59184 -1.26739,1.31519l0,2.63038l-1.26739,0c-0.69706,0 -1.26739,0.59184 -1.26739,1.31519l0,21.04303c0,0.72335 0.57033,1.31519 1.26739,1.31519l7.60434,0l0,3.94557l-5.06956,0c-0.69706,0 -1.26739,0.59184 -1.26739,1.31519l0,9.20633l-12.6739,0l0,-1.31519c0,-0.72335 -0.57033,-1.31519 -1.26739,-1.31519l-7.60434,0c-0.69706,0 -1.26739,0.59184 -1.26739,1.31519l0,31.56456c0,0.72335 0.57033,1.31519 1.26739,1.31519l7.60434,0c0.69706,0 1.26739,-0.59184 1.26739,-1.31519l0,-1.31519l55.76515,0l0,1.31519c0,0.72335 0.57033,1.31519 1.26739,1.31519l7.60434,0c0.69706,0 1.26739,-0.59184 1.26739,-1.31519l0,-31.56456c0,-0.72335 -0.57033,-1.31519 -1.26739,-1.31519l0.00001,0zm-1.26739,15.78228l-5.06956,0l0,-5.26076l5.06956,0l0,5.26076zm-5.06956,2.63038l5.06956,0l0,5.26076l-5.06956,0l0,-5.26076zm0,-15.78228l5.06956,0l0,5.26076l-5.06956,0l0,-5.26076zm-25.3478,-17.09747l0,-18.41266l2.53478,0l0,18.41266l-2.53478,0zm-5.06956,0l0,-18.41266l2.53478,0l0,18.41266l-2.53478,0zm-5.06956,0l0,-18.41266l2.53478,0l0,18.41266l-2.53478,0zm-5.06956,0l0,-18.41266l2.53478,0l0,18.41266l-2.53478,0zm20.27824,-18.41266l2.53478,0l0,18.41266l-2.53478,0l0,-18.41266zm7.60434,18.41266l-2.53478,0l0,-18.41266l2.53478,0l0,18.41266zm-30.41736,-22.35823l27.88258,0l0,1.31519l-27.88258,0l0,-1.31519zm-2.53478,3.94557l2.53478,0l0,18.41266l-2.53478,0l0,-18.41266zm8.87173,21.04303l15.20868,0l0,3.94557l-15.20868,0l0,-3.94557zm-24.08041,27.61899l-5.06956,0l0,-5.26076l5.06956,0l0,5.26076zm-5.06956,2.63038l5.06956,0l0,5.26076l-5.06956,0l0,-5.26076zm0,-15.78228l5.06956,0l0,5.26076l-5.06956,0l0,-5.26076zm5.06956,28.93417l-5.06956,0l0,-5.26076l5.06956,0l0,5.26076zm2.53478,-2.63038l0,-23.67342l13.94129,0c0.69706,0 1.26739,-0.59184 1.26739,-1.31519l0,-9.20633l27.88258,0l0,9.20633c0,0.72335 0.57033,1.31519 1.26739,1.31519l11.40651,0l0,23.67342l-55.76515,0l-0.00001,0zm63.36949,2.63038l-5.06956,0l0,-5.26076l5.06956,0l0,5.26076z" fill="black" id="svg_5"/>
             </g>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_ACTUATOR_PORT = "actuatorPortId"
        const val FIELD_OPENING_TIME = "openingTime"
        const val FIELD_INACTIVE_STATE = "inactivityState"
        const val STATE_ENABLED = "enabled"
        const val STATE_DISABLED = "disabled"
        const val NOTE_VALVE_OPENING = "valve"
        const val NOTE_RELAY_STATE = "relay"
    }
}