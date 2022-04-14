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

package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.fields.InstanceReference
import eu.automateeverything.data.fields.InstanceReferenceType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.Relay
import org.pf4j.Extension

@Extension
class CentralHeatingPumpConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {
    override val parent: Class<out Configurable> = CentralHeatingConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_central_heating_pump_add

    override val editRes: Resource
        get() = R.configurable_central_heating_pump_edit

    override val titleRes: Resource
        get() = R.configurable_central_heating_pumps_title

    override val descriptionRes: Resource
        get() = R.configurable_central_heating_pumps_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_TRANSFORMER_PORT] = transformerPortField
            result[FIELD_PUMP_PORT] = pumpPortField
            result[FIELD_THERMAL_ACTUATORS] = thermalActuatorIdsField
            return result
        }

    private val thermalActuatorIdsField = InstanceReferenceField(
        FIELD_THERMAL_ACTUATORS, R.field_thermal_actuators_hint,
        InstanceReference(ThermalActuatorConfigurable::class.java, InstanceReferenceType.Multiple),
        RequiredStringValidator()
    )

    private val pumpPortField = RelayOutputPortField(FIELD_PUMP_PORT, R.field_pump_port_hint)

    private val transformerPortField = RelayOutputPortField(FIELD_TRANSFORMER_PORT, R.field_transformer_port_hint)

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = State.buildReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_PUMPING] = State.buildReadOnlyState(
                STATE_PUMPING,
                R.state_pumping
            )
            states[STATE_REGULATION] = State.buildReadOnlyState(
                STATE_REGULATION,
                R.state_regulation,
            )
            states[STATE_STANDBY] = State.buildReadOnlyState(
                STATE_STANDBY,
                R.state_standby
            )
            return states
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = extractFieldValue(instance, nameField)
        val pumpPortRaw = extractFieldValue(instance, pumpPortField)
        val pumpPort =
            if (pumpPortRaw != "") {
                portFinder.searchForOutputPort(Relay::class.java, pumpPortRaw)
            } else {
                null
            }

        val transformerPortRaw = extractFieldValue(instance, transformerPortField)
        val transformerPort =
            if (transformerPortRaw != "") {
                portFinder.searchForOutputPort(Relay::class.java, transformerPortRaw)
            } else {
                null
            }

        val thermalActuatorIdsRaw = extractFieldValue(instance, thermalActuatorIdsField)
        val thermalActuatorIds = thermalActuatorIdsRaw.split(",").map { it.toLong() }

        return CentralHeatingPumpAutomationUnit(stateChangeReporter, instance, name, states, pumpPort,
            transformerPort, thermalActuatorIds)
    }

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Layer 1</title>
              <g id="svg_20">
               <path d="m17.10065,13.42552l66.15062,0l0,6.16584l-66.15062,0l0,-6.16584z" fill="black" id="svg_18"/>
               <path d="m21.54544,42.88501l-11.92843,0c-0.96203,0.00069 -1.7424,0.78105 -1.74308,1.74308l0,1.1382c0.00069,0.96203 0.78105,1.7424 1.74308,1.74308l11.92631,0c0.96342,0 1.74446,-0.78037 1.74514,-1.74308l0,-1.1382c-0.00069,-0.96203 -0.78105,-1.7424 -1.74308,-1.74308l0.00005,0z" id="svg_12"/>
               <path d="m12.49751,17.29679l0,22.50531l6.16584,0l0,-15.23437c0.00138,-1.27377 0.50854,-2.49452 1.40933,-3.39526s2.12158,-1.40796 3.39526,-1.40933l1.36117,0l0,-6.16584l-8.63217,0c-0.9813,0 -1.92198,0.38949 -2.61572,1.08383c-0.69434,0.69366 -1.08383,1.63435 -1.08383,2.61572l0.00012,-0.00005z" fill="black" id="svg_11"/>
               <path d="m12.49751,50.59231l6.16584,0l0,35.98194l-6.16584,0l0,-35.98194z" fill="black" id="svg_13"/>
               <path d="m30.951,24.57259l0,15.22944l6.16584,0l0,-22.50531c0,-0.9813 -0.38949,-1.92198 -1.08383,-2.61572c-0.69366,-0.69434 -1.63435,-1.08383 -2.61572,-1.08383l-8.63217,0l0,6.16584l1.35634,0c1.27515,0.00138 2.49734,0.50854 3.39879,1.41071c0.90217,0.90148 1.40933,2.12369 1.41071,3.39879l0.00005,0.00009z" fill="black" id="svg_25"/>
               <path d="m39.99893,42.88512l-11.92843,0c-0.96203,0.00069 -1.7424,0.78105 -1.74308,1.74308l0,1.1382c0.00069,0.96203 0.78105,1.7424 1.74308,1.74308l11.92631,0c0.96342,0 1.74446,-0.78037 1.74514,-1.74308l0,-1.1382c-0.00069,-0.96203 -0.78105,-1.7424 -1.74308,-1.74308l0.00005,0z" fill="black" id="svg_23"/>
               <path d="m30.951,50.59255l6.16584,0l0,35.98194l-6.16584,0l0,-35.98194z" fill="black" id="svg_24"/>
               <path d="m47.86343,24.46689l0,15.22944l6.16584,0l0,-22.50531c0,-0.9813 -0.38949,-1.92198 -1.08383,-2.61572c-0.69366,-0.69434 -1.63435,-1.08383 -2.61572,-1.08383l-8.63217,0l0,6.16584l1.35634,0c1.27515,0.00138 2.49734,0.50854 3.39879,1.41071c0.90217,0.90148 1.40933,2.12369 1.41071,3.39879l0.00005,0.00009z" fill="black" id="svg_9"/>
               <path d="m56.91135,42.77943l-11.92843,0c-0.96203,0.00069 -1.7424,0.78105 -1.74308,1.74308l0,1.1382c0.00069,0.96203 0.78105,1.7424 1.74308,1.74308l11.92631,0c0.96342,0 1.74446,-0.78037 1.74514,-1.74308l0,-1.1382c-0.00069,-0.96203 -0.78105,-1.7424 -1.74308,-1.74308l0.00005,0z" fill="black" id="svg_8"/>
               <path d="m47.86343,50.48685l6.16584,0l0,35.98194l-6.16584,0l0,-35.98194z" fill="black" id="svg_7"/>
               <path d="m64.77544,24.46689l0,15.22944l6.16584,0l0,-22.50531c0,-0.9813 -0.38949,-1.92198 -1.08383,-2.61572c-0.69366,-0.69434 -1.63435,-1.08383 -2.61572,-1.08383l-8.63217,0l0,6.16584l1.35634,0c1.27515,0.00138 2.49734,0.50854 3.39879,1.41071c0.90217,0.90148 1.40933,2.12369 1.41071,3.39879l0.00005,0.00009z" fill="black" id="svg_15"/>
               <path d="m73.82336,42.77943l-11.92843,0c-0.96203,0.00069 -1.7424,0.78105 -1.74308,1.74308l0,1.1382c0.00069,0.96203 0.78105,1.7424 1.74308,1.74308l11.92631,0c0.96342,0 1.74446,-0.78037 1.74514,-1.74308l0,-1.1382c-0.00069,-0.96203 -0.78105,-1.7424 -1.74308,-1.74308l0.00005,0z" fill="black" id="svg_14"/>
               <path d="m64.77544,50.48685l6.16584,0l0,35.98194l-6.16584,0l0,-35.98194z" fill="black" id="svg_10"/>
               <path d="m81.33511,24.46689l0,15.22944l6.16584,0l0,-22.50531c0,-0.9813 -0.38949,-1.92198 -1.08383,-2.61572c-0.69366,-0.69434 -1.63435,-1.08383 -2.61572,-1.08383l-8.63217,0l0,6.16584l1.35634,0c1.27515,0.00138 2.49734,0.50854 3.39879,1.41071c0.90217,0.90148 1.40933,2.12369 1.41071,3.39879l0.00005,0.00009z" fill="black" id="svg_19"/>
               <path d="m90.38304,42.77943l-11.92843,0c-0.96203,0.00069 -1.7424,0.78105 -1.74308,1.74308l0,1.1382c0.00069,0.96203 0.78105,1.7424 1.74308,1.74308l11.92631,0c0.96342,0 1.74446,-0.78037 1.74514,-1.74308l0,-1.1382c-0.00069,-0.96203 -0.78105,-1.7424 -1.74308,-1.74308l0.00005,0z" fill="black" id="svg_17"/>
               <path d="m81.33511,50.48685l6.16584,0l0,35.98194l-6.16584,0l0,-35.98194z" fill="black" id="svg_16"/>
              </g>
             </g>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_PUMP_PORT = "pumpPortId"
        const val FIELD_TRANSFORMER_PORT = "transformerPortId"
        const val FIELD_THERMAL_ACTUATORS = "thermalActuatorIds"
        const val STATE_PUMPING = "pumping"
        const val STATE_REGULATION = "regulation"
        const val STATE_STANDBY = "standby"
    }
}