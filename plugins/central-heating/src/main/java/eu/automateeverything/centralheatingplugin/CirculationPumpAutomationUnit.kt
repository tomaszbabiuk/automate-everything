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

import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_OFF
import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_PUMPING
import eu.automateeverything.centralheatingplugin.CirculationPumpConfigurable.Companion.STATE_STANDBY
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.SensorAutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay
import eu.automateeverything.domain.hardware.Temperature
import java.math.BigDecimal
import java.util.*

class CirculationPumpAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val pumpPort: OutputPort<Relay>,
    minWorkingTime: Duration,
    private val thermometerId: Long
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private lateinit var thermometerUnit: SensorAutomationUnit<Temperature>
    override val usedPortsIds = arrayOf(pumpPort.id)

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = false
    private var switchedOffTime = Calendar.getInstance().timeInMillis

    private val temperatureCheckInterval = (90 * 1000).toLong()
    private var lastTemperatureCheck: Long = 0
    private var lastTemperatureMeasured: Temperature? = null

    override fun applyNewState(state: String) {
    }

    private var lastSwitchingOnTime: Calendar? = null

    private fun isOn(): Boolean {
        return pumpPort.read().value == BigDecimal.ONE
    }

    private val minWorkingTimeCounter = MinimumWorkingTimeCounter(
        pumpPort.read().value == BigDecimal.ONE,
        minWorkingTime.milliseconds
    )

    override fun calculateInternal(now: Calendar) {
        if (thermometerUnit.lastEvaluation.value == null) {
            return
        }

        if (lastTemperatureMeasured == null) {
            lastTemperatureMeasured = thermometerUnit.lastEvaluation.value
        }

        val nowMillis = now.timeInMillis
        if (currentState.id == STATE_PUMPING || currentState.id == STATE_STANDBY) {
            if (nowMillis - switchedOffTime > temperatureCheckInterval * 3 && !isOn()) {
                lastTemperatureCheck = nowMillis
                lastTemperatureMeasured = thermometerUnit.lastEvaluation.value
                changeState(STATE_PUMPING)
            } else {
                if (nowMillis - lastTemperatureCheck > temperatureCheckInterval) {
                    lastTemperatureCheck = nowMillis
                    val currentTemperature = thermometerUnit.lastEvaluation.value
                    val temperatureDelta = currentTemperature!!.value.subtract(lastTemperatureMeasured!!.value)
                    val keepPumping = temperatureDelta > BigDecimal.ONE
                    if (keepPumping) {
                        if (!isOn()) {
                            lastSwitchingOnTime = now
                            changeState(STATE_PUMPING)
                        }
                    } else {
                        if (isOn()) {
                            switchedOffTime = nowMillis
                            changeState(STATE_STANDBY)
                        }
                    }
                    lastTemperatureMeasured = currentTemperature
                }
            }
        } else {
            changeState(STATE_OFF)
        }

        minWorkingTimeCounter.signal(pumpPort.read().value == BigDecimal.ONE)

        val enablePump = currentState.id == STATE_PUMPING
        if (enablePump) {
            pumpPort.write(Relay.ON)
        } else if (minWorkingTimeCounter.isExceeded) {
            pumpPort.write(Relay.OFF)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
        thermometerUnit = automationUnitsCache[thermometerId]!!.second as SensorAutomationUnit<Temperature>
    }
}
