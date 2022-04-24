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

package eu.automateeverything.onoffplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.PortFinder
import eu.automateeverything.domain.hardware.Relay
import org.pf4j.Extension

@Extension
class TimedOnOffDeviceConfigurable(
    private val portFinder: PortFinder,
    private val eventBus: EventBus
) : StateDeviceConfigurable() {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_PORT] = portField
            result[FIELD_AUTOMATION_ONLY] = automationOnlyField
            result[FIELD_MIN_TIME] = minTimeField
            result[FIELD_MAX_TIME] = maxTimeField
            result[FIELD_BREAK_TIME] = breakTimeField
            return result
        }

    override val parent: Class<out Configurable>
        get() = OnOffDevicesConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_timedonoffdevice_add

    override val editRes: Resource
        get() = R.configurable_timedonoffdevice_edit

    override val titleRes: Resource
        get() = R.configurable_timedonoffdevice_title

    override val descriptionRes: Resource
        get() = R.configurable_timedonoffdevices_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Layer 1</title>
              <path d="m4,53.572c-0.016,-12.872 5.321,-22.61 10.48,-28.792l0,0c5.187,-6.233 10.224,-9.236 10.676,-9.516l0,0c3.368,-1.993 7.697,-0.849 9.667,2.555l0,0c1.966,3.392 0.851,7.747 -2.489,9.75l0,0l0.001,0.001c0,0 -0.004,0.001 -0.015,0.008l0,0c-0.009,0.004 -0.023,0.014 -0.042,0.026l0,0c-0.068,0.042 -0.194,0.125 -0.367,0.244l0,0c-0.351,0.234 -0.897,0.625 -1.572,1.162l0,0c-1.349,1.073 -3.203,2.74 -5.029,4.945l0,0c-3.669,4.467 -7.166,10.798 -7.181,19.616l0,0c0.003,8.889 3.547,16.882 9.314,22.722l0,0c5.78,5.83 13.687,9.416 22.48,9.418l0,0c8.793,-0.002 16.699,-3.588 22.478,-9.418l0,0c5.769,-5.84 9.313,-13.833 9.316,-22.722l0,0c-0.01601,-8.548 -3.29401,-14.743 -6.83701,-19.195l0,0c-3.37299,-4.221 -6.98199,-6.572 -7.341,-6.791l0,0c-0.004,-0.003 -0.009,-0.004 -0.012,-0.006l0,0c-0.005,-0.003 -0.008,-0.005 -0.01099,-0.006l0,0c-0.00301,-0.003 -0.007,-0.003 -0.007,-0.003l0,0l0,0c-3.336,-2.005 -4.452,-6.36 -2.488,-9.752l0,0c1.972,-3.404 6.3,-4.548 9.667,-2.555l0,0c0.45399,0.279 5.491,3.282 10.67799,9.516l0,0c5.16199,6.183 10.49599,15.92 10.481,28.792l0,0c-0.004,25.637 -20.564,46.42 -45.923,46.429l0,0c-25.359,-0.009 -45.918,-20.792 -45.924,-46.428l0,0l0.00002,0l-0.00001,0z" fill="black" id="svg_1"/>
              <path d="m42.859,20.4999l0,-7.9289l0,-7.92816c0,-1.45924 3.163,-2.64284 7.066,-2.64284l0,0c3.899,0 7.062,1.1836 7.062,2.64284l0,0l0,7.92816l0,7.9289l0.002,0c0,1.45813 -3.165,2.6421 -7.064,2.6421l0,0c-3.903,0 -7.066,-1.18397 -7.066,-2.6421l0,0z" id="svg_2"/>
              <g id="svg_3">
               <g id="svg_4">
                <g id="svg_5">
                 <rect fill="#000000" height="6.41022" id="svg_6" width="1.60016" x="48.94931" y="69.99197"/>
                </g>
                <g id="svg_7">
                 <rect fill="#000000" height="10.014" id="svg_8" transform="matrix(0.553718 0.321055 -0.321055 0.553718 31.5914 0.28932)" width="2.5" x="65.2186" y="83.39215"/>
                </g>
                <g id="svg_9">
                 <rect fill="#000000" height="2.5" id="svg_10" transform="matrix(0.553974 0.320607 -0.320607 0.553974 17.3292 0.254692)" width="10.015" x="50.43148" y="45.781"/>
                </g>
                <g id="svg_11">
                 <rect fill="#000000" height="2.5" id="svg_12" transform="matrix(0.319647 0.554486 -0.554486 0.319647 28.5423 -6.60761)" width="10.014" x="63.00115" y="17.17822"/>
                </g>
                <g id="svg_13">
                 <rect fill="#000000" height="2.5" id="svg_14" transform="matrix(0.319647 0.55455 -0.55455 0.319647 67.3643 -6.56613)" width="10.015" x="93.26694" y="69.68907"/>
                </g>
                <g id="svg_15">
                 <rect fill="#000000" height="1.60016" id="svg_16" width="6.41022" x="27.14751" y="52.9996"/>
                </g>
                <g id="svg_17">
                 <rect fill="#000000" height="10.018" id="svg_18" transform="matrix(0.318111 0.555446 -0.555446 0.318111 47.8496 13.0145)" width="2.501" x="55.32749" y="54.56222"/>
                </g>
                <path d="m49.74939,26.23732c-15.19764,0 -27.56236,12.36408 -27.56236,27.56236c0,15.19828 12.36408,27.563 27.56236,27.563c15.19892,0 27.56364,-12.36408 27.56364,-27.563c0,-15.19764 -12.36472,-27.56236 -27.56364,-27.56236zm11.98389,8.38162l-3.20223,5.55318l-1.38573,-0.79944l3.20223,-5.55318l1.38573,0.79944zm7.1975,7.19814l0.79944,1.38637l-5.55254,3.20223l-0.79944,-1.38637l5.55254,-3.20223zm-2.98909,11.18253l6.41022,0l0,1.60016l-6.41022,0l0,-1.60016zm-16.19229,24.52271c-13.08031,0 -23.72199,-10.64167 -23.72199,-23.72263c0,-12.81213 10.21155,-23.27714 22.92191,-23.70151l0,23.70151c0,0.28035 0.14657,0.54021 0.3866,0.68487l20.28421,12.25015c-4.23785,6.48831 -11.55952,10.78761 -19.87073,10.78761z" fill="#000000" id="svg_19"/>
               </g>
              </g>
             </g>
            </svg>
        """.trimIndent()

    private val portField = RelayOutputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator())

    private val minTimeField = DurationField(FIELD_MIN_TIME, R.field_min_working_time, Duration(0))

    private val automationOnlyField = BooleanField(FIELD_AUTOMATION_ONLY, R.field_automation_only_hint, false)

    private val maxTimeField = DurationField(FIELD_MAX_TIME, R.field_max_working_time, Duration(0), object: Validator<Duration> {
        override val reason: Resource
            get() = R.validator_max_should_exceed_min_time

        override fun validate(validatedFieldValue: Duration?, allFields: Map<String, String?>): Boolean {
            if (validatedFieldValue == null) {
                return true
            }

            val minTimeFieldValueRaw = allFields[FIELD_MIN_TIME]
            val minTimeFieldValue = minTimeField.builder.fromPersistableString(minTimeFieldValueRaw)

            if (minTimeFieldValue.seconds == 0) {
                return true
            }

            return (validatedFieldValue.seconds > minTimeFieldValue.seconds)
        }
    })

    private val breakTimeField = DurationField(FIELD_BREAK_TIME, R.field_break_time, Duration(0), object: Validator<Duration> {
        override val reason: Resource
            get() = R.validator_break_invalid_if_no_max_time

        override fun validate(validatedFieldValue: Duration?, allFields: Map<String, String?>): Boolean {
            if (validatedFieldValue == null) {
                return true
            }

            if (validatedFieldValue.seconds == 0) {
                return true
            }

            val maxTimeFieldValueRaw = allFields[FIELD_MAX_TIME]
            val maxTimeFieldValue = maxTimeField.builder.fromPersistableString(maxTimeFieldValueRaw)
            return (maxTimeFieldValue.seconds > 0 && validatedFieldValue.seconds > 0)
        }
    })

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val portId = extractFieldValue(instance, portField)
        val port = portFinder.searchForOutputPort(Relay::class.java, portId)
        val name = extractFieldValue(instance, nameField)
        val minWorkingTime = extractFieldValue(instance, minTimeField)
        val maxWorkingTime = extractFieldValue(instance, maxTimeField)
        val breakTime = extractFieldValue(instance, breakTimeField)
        val automationOnly = extractFieldValue(instance, automationOnlyField)
        return TimedOnOffDeviceAutomationUnit(
            eventBus,
            instance,
            name,
            minWorkingTime,
            maxWorkingTime,
            breakTime,
            states,
            port,
            automationOnly)
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = LinkedHashMap()
            states[STATE_UNKNOWN] = State.buildReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_ON] = State.buildControlState(
                STATE_ON,
                R.state_on,
                R.action_on,
                isSignaled = true,
            )
            states[STATE_ON_COUNTING] = State.buildReadOnlyState(
                STATE_ON_COUNTING,
                R.state_on_counting,
                isSignaled = true,
            )
            states[STATE_OFF_BREAK] = State.buildReadOnlyState(
                STATE_OFF_BREAK,
                R.state_off_break,
                isSignaled = true,
            )
            states[STATE_OFF] = State.buildControlState(
                STATE_OFF,
                R.state_forced_off,
                R.action_off,
            )
            return states
        }

    companion object {
        const val FIELD_MIN_TIME = "minTime"
        const val FIELD_MAX_TIME = "maxTime"
        const val FIELD_BREAK_TIME = "breakTime"
        const val FIELD_PORT = "portId"
        const val FIELD_AUTOMATION_ONLY = "automationOnly"
        const val STATE_ON = "on"
        const val STATE_ON_COUNTING = "on_counting"
        const val STATE_OFF = "off"
        const val STATE_OFF_BREAK = "off_break"
    }
}