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

package eu.automateeverything.alarmplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.fields.InstanceReference
import eu.automateeverything.data.fields.InstanceReferenceType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.events.EventBus
import org.pf4j.Extension

@Extension
class AlarmZoneConfigurable(
    private val eventBus: EventBus
) : StateDeviceConfigurable() {

    override val parent: Class<out Configurable>
        get() = AlarmDevicesConfigurable::class.java

    private val alarmLinesField = InstanceReferenceField(FIELD_ALARM_LINES, R.field_alarm_lines_hint,
        InstanceReference(AlarmLineConfigurable::class.java, InstanceReferenceType.Multiple),
        RequiredStringValidator()
    )

    private val leavingTimeField = DurationField(FIELD_LEAVING_TIME, R.field_leaving_time_hint,
        Duration(0)
    )

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = extractFieldValue(instance, nameField)
        val alarmLineIdsRaw = extractFieldValue(instance, alarmLinesField)
        val alarmLineIds = alarmLineIdsRaw.split(",").map { it.toLong() }
        val leavingTime = extractFieldValue(instance, leavingTimeField)
        return AlarmZoneAutomationUnit(eventBus, instance, name, states, leavingTime, alarmLineIds)
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = State.buildReadOnlyState(
                STATE_UNKNOWN,
                R.state_unknown,
            )
            states[STATE_DISARMED] = State.buildControlState(
                STATE_DISARMED,
                R.state_disarmed,
                R.action_disarm
            )
            states[STATE_ARMED] = State.buildControlState(
                STATE_ARMED,
                R.state_armed,
                R.action_arm,
            )
            states[STATE_LEAVING] = State.buildControlState(
                STATE_LEAVING,
                R.state_leaving,
                R.action_count
            )
            states[STATE_PREALARM] = State.buildReadOnlyState(
                STATE_PREALARM,
                R.state_prealarm,
                isSignaled = true
            )
            states[STATE_ALARM] = State.buildReadOnlyState(
                STATE_ALARM,
                R.state_alarm,
                isSignaled = true
            )
            return states
        }

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_LEAVING_TIME] = leavingTimeField
            result[FIELD_ALARM_LINES] = alarmLinesField
            return result
        }

    override val addNewRes = R.configurable_alarmzone_add
    override val editRes = R.configurable_alarmzone_edit
    override val titleRes = R.configurable_alarmzone_title
    override val descriptionRes = R.configurable_alarmzone_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g>
              <title>Layer 1</title>
              <path id="svg_2" d="m92.115,16.646c-0.541,-0.127 -1.14,-0.271 -1.793,-0.431c-2.88,-0.708 -6.786,-1.746 -11.191,-3.126c-0.555,-0.174 -1.117,-0.353 -1.687,-0.538c-8.805,-2.855 -19.24,-7.012 -27.362,-12.551c-17.37,11.713 -45.472,17.369 -45.472,17.369s0.084,24.041 1.591,33.582c1.435,9.08 5.127,16.719 9.795,23.048c0.315,0.428 0.635,0.851 0.959,1.266c2.141,2.748 4.459,5.235 6.841,7.476c0.385,0.362 0.77,0.719 1.158,1.066c12,10.809 25.127,15.476 25.127,15.476s38.241,-13.652 43.719,-48.332c1.508,-9.542 1.591,-33.582 1.591,-33.582s-1.208,-0.24 -3.276,-0.723zm-1.032,33.875c-1.179,7.459 -4.077,14.507 -8.617,20.946c-3.66901,5.20599 -8.416,10.03699 -14.109,14.362c-8.006,6.07999 -15.686,9.453 -18.276,10.501c-2.603,-1.04601 -10.315,-4.42101 -18.357,-10.50401c-1.691,-1.279 -3.297,-2.605 -4.819,-3.96999c-0.393,-0.35101 -0.78,-0.705 -1.161,-1.062c-2.541,-2.384 -4.821,-4.888 -6.827,-7.493c-0.324,-0.42101 -0.639,-0.846 -0.95,-1.272c-0.136,-0.188 -0.275,-0.375 -0.409,-0.565c-4.555,-6.438 -7.461,-13.484 -8.64,-20.942c-1.192,-7.55 -1.475,-24.8 -1.539,-30.941c2.809,-0.643 8.027,-1.923 14.274,-3.878c11.615,-3.635 21.164,-7.805 28.42,-12.41c6.583,4.224 15.037,8.063 25.191,11.439c0.552,0.184 1.112,0.366 1.675,0.547c0.493,0.158 0.988,0.317 1.489,0.473c3.657,1.144 6.957,2.05 9.625,2.732c0.63,0.161 1.224,0.309 1.778,0.446c1.088,0.266 2.027,0.485 2.79,0.657c-0.063,6.148 -0.346,23.388 -1.538,30.934z"/>
              <path id="svg_4" d="m61.838,40.458l-0.983,0l0,-3.645c0,-5.891 -4.793,-10.685 -10.685,-10.685c-5.891,0 -10.684,4.793 -10.684,10.685l0,3.645l-0.982,0c-1.954,0 -3.538,1.583 -3.538,3.537l0,3.666l30.41,0l0,-3.666c-0.001,-1.953 -1.586,-3.537 -3.538,-3.537zm-5.307,0l-12.722,0l0,-3.645c0,-3.508 2.854,-6.361 6.361,-6.361c3.508,0 6.362,2.854 6.362,6.361l0,3.645l-0.001,0z"/>
              <rect id="svg_5" height="7.785" width="30.41" y="49.233" x="34.965"/>
              <path id="svg_6" d="m34.965,61.589c0,1.954 1.584,3.538 3.538,3.538l23.335,0c1.952,0 3.537,-1.584 3.537,-3.538l0,-2.999l-30.41,0l0,2.999z"/>
             </g>
            </svg>
        """.trimIndent()

    companion object {
        const val FIELD_ALARM_LINES = "alarmLines"
        const val FIELD_LEAVING_TIME = "leavingTime"
        const val STATE_DISARMED = "disarmed"
        const val STATE_ARMED = "armed"
        const val STATE_LEAVING = "leaving"
        const val STATE_PREALARM = "prealarm"
        const val STATE_ALARM = "alarm"
    }
}

