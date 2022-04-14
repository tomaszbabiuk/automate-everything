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
import eu.automateeverything.sensorsandcontrollersplugin.ThermometerConfigurable
import org.pf4j.Extension

@Extension
class CirculationPumpConfigurable(
    private val portFinder: PortFinder,
    private val stateChangeReporter: StateChangeReporter
) : StateDeviceConfigurable() {

    override val parent: Class<out Configurable> = CentralHeatingConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_circulation_pump_add

    override val editRes: Resource
        get() = R.configurable_circulation_pump_edit

    override val titleRes: Resource
        get() = R.configurable_circulation_pumps_title

    override val descriptionRes: Resource
        get() = R.configurable_circulation_pumps_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PUMP_PORT] = pumpPortField
            result[FIELD_MIN_WORKING_TIME] = minWorkingTimeField
            result[FIELD_THERMOMETER_ID] = thermometerIdField
            return result
        }

    private val pumpPortField = RelayOutputPortField(ThermalActuatorConfigurable.FIELD_ACTUATOR_PORT, R.field_pump_port_hint, RequiredStringValidator())

    private val thermometerIdField = InstanceReferenceField(
        FIELD_THERMOMETER_ID, R.field_thermometer_hint,
        InstanceReference(ThermometerConfigurable::class.java, InstanceReferenceType.Single),
        RequiredStringValidator()
    )

    private val minWorkingTimeField = DurationField(
        FIELD_MIN_WORKING_TIME, R.field_minimum_working_time_hint,
        Duration(10)
    )

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
            states[STATE_STANDBY] = State.buildControlState(
                STATE_STANDBY,
                R.state_standby,
                R.action_standby
            )
            states[STATE_OFF] = State.buildControlState(
                STATE_OFF,
                R.state_off,
                R.action_off
            )
            return states
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = extractFieldValue(instance, nameField)
        val minWorkingTime = extractFieldValue(instance, minWorkingTimeField)
        val pumpPortRaw = extractFieldValue(instance, pumpPortField)
        val pumpPort = portFinder.searchForOutputPort(Relay::class.java, pumpPortRaw)
        val thermometerId = extractFieldValue(instance, thermometerIdField)

        return CirculationPumpAutomationUnit(stateChangeReporter, instance, name, states, pumpPort,
            minWorkingTime, thermometerId.toLong())
    }

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Layer 1</title>
              <g id="svg_1">
               <path d="m19.101,25.929c1.406,-1.919 2.978,-3.719 4.7,-5.354c7.066,-6.67 16.956,-10.688 26.729,-10.354c10.256,0.352 20.029,4.38 27.177,11.865l-4.915,4.661c-6.335,-6.658 -15.546,-10.279 -24.722,-9.717c-8.947,0.562 -17.325,4.939 -22.834,12.002l2.837,1.441l-11.303,7.371l-0.728,-13.454l3.059,1.539zm54.674,23.259c0,13.244 -10.801,24.012 -24.071,24.012c-13.265,0 -24.047,-10.769 -24.047,-24.012c0,-2.369 0.358,-4.753 1.044,-7.06c0.123,-0.407 0.527,-0.661 0.942,-0.611c0.281,0.046 0.499,0.218 0.622,0.457c1.382,1.417 3.228,2.19 5.214,2.19c1.74,0 3.329,-0.573 4.623,-1.652c0.306,-0.415 1.009,-0.45 1.354,-0.053c1.308,1.125 2.942,1.705 4.711,1.705c1.881,0 3.61,-0.679 4.922,-1.916c0.246,-0.246 0.604,-0.316 0.921,-0.187c0.151,0.073 0.274,0.187 0.362,0.316c1.308,1.153 3.013,1.786 4.815,1.786c1.905,0 3.728,-0.731 5.027,-2.015c0.246,-0.253 0.615,-0.323 0.932,-0.193c0.158,0.07 0.289,0.179 0.377,0.312c1.312,1.228 3.059,1.896 4.914,1.896c1.854,0 3.656,-0.714 4.976,-1.966c0.334,-0.302 0.844,-0.302 1.167,-0.014c0.115,0.105 0.238,0.288 0.292,0.439c0.599,2.148 0.903,4.362 0.903,6.566zm-27.193,40.61l-0.218,-3.421c-16.218,-1.554 -29.9,-13.454 -33.574,-29.443c-1.54,-6.65 -1.266,-13.612 0.798,-20.099l6.444,2.053c-1.688,5.337 -1.927,11.05 -0.668,16.53c2.953,12.797 13.683,22.416 26.561,24.098l-0.222,-3.18l12.111,5.975l-11.232,7.487zm11.988,-4.23l-1.52,-6.596c16.883,-3.886 27.465,-20.781 23.572,-37.656c-0.422,-1.804 -0.995,-3.579 -1.723,-5.264l-2.631,1.79l-1,-13.45l12.164,5.86l-2.848,1.927c1.125,2.43 2.021,4.985 2.63,7.622c4.72,20.501 -8.118,41.039 -28.644,45.767z" id="svg_2"/>
              </g>
             </g>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_PUMP_PORT = "pumpPortId"
        const val FIELD_THERMOMETER_ID = "thermometerId"
        const val FIELD_MIN_WORKING_TIME = "minWorkingTime"
        const val STATE_PUMPING = "pumping"
        const val STATE_STANDBY = "standby"
        const val STATE_OFF = "off"
    }
}