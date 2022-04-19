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

package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import java.math.BigDecimal
import java.util.*

open class AutomationUnitWrapper<T>(
    @Suppress("UNUSED_PARAMETER") valueClazz: Class<T>,
    val stateChangeReporter: StateChangeReporter,
    name: String,
    val instance: InstanceDto,
    initError: AutomationErrorException
) : AutomationUnitBase<T>(stateChangeReporter, name, instance, ControlType.NA, EvaluationResult(
    interfaceValue = R.error_initialization,
    error = initError,
    descriptions = listOf(initError.localizedMessage)
)) {

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false

}

class StateDeviceAutomationUnitWrapper(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    initError: AutomationErrorException
) : AutomationUnitWrapper<State>(State::class.java, stateChangeReporter, name, instance, initError),
StateDeviceAutomationUnit{
    override fun changeState(state: String, actor: String?) {
    }

    override val currentState: State
        get() = State.buildReadOnlyState("error", Resource.createUniResource("error"))
}

class ControllerAutomationUnitWrapper<V: PortValue>(
    override val valueClazz: Class<V>,
    stateChangeReporter: StateChangeReporter,
    name: String,
    instance: InstanceDto,
    initError: AutomationErrorException
) : AutomationUnitWrapper<V>(valueClazz, stateChangeReporter, name, instance, initError, ), ControllerAutomationUnit<V> {

    override val usedPortsIds = arrayOf<String>()

    override fun calculateInternal(now: Calendar) {
    }

    override fun control(newValue: V, actor: String?) {
    }

    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = BigDecimal.ZERO
    override val step: BigDecimal = BigDecimal.ZERO
}
