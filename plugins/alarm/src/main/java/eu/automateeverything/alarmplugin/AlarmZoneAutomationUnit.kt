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

import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_ALARM
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_ARMED
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_DISARMED
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_LEAVING
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_PREALARM
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.automateeverything.domain.events.EventsSink
import java.util.*

class AlarmZoneAutomationUnit(
    eventsSink: EventsSink,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val leavingTime: Duration,
    private val alarmLineIds: List<Long>,
) : StateDeviceAutomationUnitBase(eventsSink, instance, name, ControlType.States, states, false) {

    override fun applyNewState(state: String) {
        when (state) {
            STATE_DISARMED -> {
                disarmAlarmSensors()
            }
            STATE_ARMED -> {
                armAlarmSensors()
                sensorThatCausedTheAlarm = null
            }
            STATE_LEAVING -> {
                leavingStartedAtTicks = Calendar.getInstance().timeInMillis
            }
            STATE_PREALARM -> {
            }
            STATE_ALARM -> {
            }
        }
    }

    private lateinit var alarmLineUnits: List<AlarmLineAutomationUnit>

    private var sensorThatCausedTheAlarm: AlarmLineAutomationUnit? = null
    private var leavingStartedAtTicks: Long = Long.MAX_VALUE

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    override fun calculateInternal(now: Calendar) {
        if (currentState.id == STATE_UNKNOWN) {
            changeState(STATE_DISARMED)
            return
        }

        if (currentState.id == STATE_ARMED) {
            for (alarmSensorAutomationUnit in alarmLineUnits) {
                if (alarmSensorAutomationUnit.isAlarm()) {
                    sensorThatCausedTheAlarm = alarmSensorAutomationUnit
                    changeState(STATE_ALARM)
                    return
                }
                if (alarmSensorAutomationUnit.isPrealarm()) {
                    changeState(STATE_PREALARM)
                    return
                }
            }
        }

        if (currentState.id === STATE_LEAVING) {
            if (now.timeInMillis > leavingStartedAtTicks + leavingTime.milliseconds) {
                changeState(STATE_ARMED)
                return
            }
        }

        if (currentState.id === STATE_PREALARM) {
            for (alarmSensorAutomationUnit in alarmLineUnits) if (alarmSensorAutomationUnit.isAlarm()) {
                sensorThatCausedTheAlarm = alarmSensorAutomationUnit
                changeState(STATE_ALARM)
                return
            }
        }
    }


    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
        alarmLineUnits = alarmLineIds.map { automationUnitsCache[it]!!.second as AlarmLineAutomationUnit }
    }

    private fun armAlarmSensors() {
        alarmLineUnits.forEach { it.arm() }
    }

    private fun disarmAlarmSensors() {
        alarmLineUnits.forEach { it.disarm() }
    }
}
