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
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.InputPort
import java.util.*

class AlarmLineAutomationUnit(
    eventBus: EventBus,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val inputPort: InputPort<BinaryInput>,
    private val contactType: ContactType,
    delayTime: Duration
) : StateDeviceAutomationUnitBase(eventBus, instance, name, ControlType.States, states, false) {

    private val armingTicks: Long = delayTime.milliseconds
    private var armingStartedAtTicks: Long = 0
    private var lastBreachedTime: Calendar? = null
    private var lineBreached = false

    override fun applyNewState(state: String) {
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf(inputPort.id)

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    private fun isLineBreached(): Boolean {
        val signal: Boolean = inputPort.read().value
        return contactType === ContactType.NO && signal || contactType === ContactType.NC && !signal
    }

    fun isAlarm(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_ALARM
    }

    fun isPrealarm(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_PREALARM
    }

    fun isWatching(): Boolean {
        return currentState.id == AlarmLineConfigurable.STATE_WATCHING
    }

    private fun causeForAlarm(): Boolean {
        return isLineBreached()
    }

    @Throws(Exception::class)
    fun arm() {
        changeState(AlarmLineConfigurable.STATE_WATCHING)
    }

    @Throws(Exception::class)
    fun disarm() {
        changeState(AlarmLineConfigurable.STATE_DISARMED)
    }

    @Throws(Exception::class)
    fun alarm() {
        changeState(AlarmLineConfigurable.STATE_ALARM)
    }

    @Throws(Exception::class)
    fun prealarm() {
        changeState(AlarmLineConfigurable.STATE_PREALARM)
    }

    override fun calculateInternal(now: Calendar) {
        if (lineBreached != isLineBreached()) {
            lastBreachedTime = now
        }
        lineBreached = isLineBreached()

        if (isWatching()) {
            if (causeForAlarm()) {
                prealarm()
                armingStartedAtTicks = now.timeInMillis
            }
        }

        if (isPrealarm() && now.timeInMillis > armingStartedAtTicks + armingTicks) {
            alarm()
        }
    }
}
