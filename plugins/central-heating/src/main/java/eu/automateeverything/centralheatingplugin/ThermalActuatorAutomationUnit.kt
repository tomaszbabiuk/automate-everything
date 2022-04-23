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

import eu.automateeverything.centralheatingplugin.ThermalActuatorConfigurable.Companion.NOTE_RELAY_STATE
import eu.automateeverything.centralheatingplugin.ThermalActuatorConfigurable.Companion.NOTE_VALVE_OPENING
import eu.automateeverything.centralheatingplugin.ThermalActuatorConfigurable.Companion.STATE_DISABLED
import eu.automateeverything.centralheatingplugin.ThermalActuatorConfigurable.Companion.STATE_ENABLED
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import java.math.BigDecimal
import java.util.*
import kotlin.math.roundToInt

class ThermalActuatorAutomationUnit(
    eventsSink: EventsSink,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val actuatorPort: OutputPort<Relay>,
    private val activationTime: Duration,
    private val inactiveState: InactiveState,
    ) : StateDeviceAutomationUnitBase(eventsSink, instance, name, ControlType.States, states, false) {

    private var openingLevel: Long = if (inactiveState == InactiveState.NO) activationTime.milliseconds else 0

    private var counter: Long = Calendar.getInstance().timeInMillis
    override val usedPortsIds = arrayOf(actuatorPort.id)

    fun isActive() = currentState.id == STATE_ENABLED

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    init {
        changeState(STATE_DISABLED)
    }

    override fun applyNewState(state: String) {
    }

    override fun calculateInternal(now: Calendar) {
        val ticksDelta: Long = now.timeInMillis - counter
        counter = now.timeInMillis
        if (actuatorPort.read().value == BigDecimal.ONE) {
            if (inactiveState == InactiveState.NO) {
                //slowly close NO actuator when powered
                if (openingLevel > 0) {
                    openingLevel -= ticksDelta
                }
            } else {
                //slowly open NC actuator when powered
                if (openingLevel < activationTime.milliseconds) {
                    openingLevel += ticksDelta
                }
            }
        } else {
            if (inactiveState == InactiveState.NO) {
                //slowly open NO actuator when powered
                if (openingLevel < activationTime.milliseconds) {
                    openingLevel += ticksDelta
                }
            } else {
                //slowly close NC actuator when powered
                if (openingLevel > 0) {
                    openingLevel -= ticksDelta
                }
            }
        }
        if (openingLevel < 0) {
            openingLevel = 0
        }
        if (openingLevel > activationTime.milliseconds) {
            openingLevel = activationTime.milliseconds
        }
    }

    fun calculateValveLevel(): Int {
        val valveOpening = (openingLevel.toDouble() / activationTime.milliseconds * 100).roundToInt()
        modifyNote(NOTE_VALVE_OPENING, R.note_opening_level(valveOpening))
        return valveOpening
    }

    fun needsARelay(): Boolean {
        return if (inactiveState == InactiveState.NC) {
            isActive()
        } else {
            !isActive()
        }
    }

    fun disableRelay() {
        if (actuatorPort.read().value != BigDecimal.ZERO) {
            actuatorPort.write(Relay.OFF)
            modifyNote(NOTE_RELAY_STATE, R.note_relay_state_disengaged)
        }
    }

    fun enableRelay() {
        if (actuatorPort.read().value == BigDecimal.ZERO) {
            actuatorPort.write(Relay.ON)
            modifyNote(NOTE_RELAY_STATE, R.note_relay_state_engaged)
        }
    }
}