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

package eu.automateeverything.actions

import eu.automateeverything.data.automation.ControlState
import eu.automateeverything.data.automation.ReadOnlyState
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.configurable.ActionConfigurable
import eu.automateeverything.domain.configurable.Configurable
import java.util.HashMap

abstract class ActionConfigurableBase(
    private val stateChangeReporter: StateChangeReporter
) : ActionConfigurable() {

    override val parent: Class<out Configurable?>
        get() = ActionsConfigurable::class.java

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = instance.fields[FIELD_NAME]!!
        return ActionAutomationUnit(stateChangeReporter, instance, name, states) {
            executionCode(instance)
        }
    }

    override val states: Map<String, State>
        get() {
            val states: MutableMap<String, State> = HashMap()
            states[STATE_UNKNOWN] = ReadOnlyState(
                STATE_UNKNOWN,
                eu.automateeverything.domain.R.state_unknown,
            )
            states[STATE_READY] = ControlState(
                STATE_READY,
                R.state_ready,
                R.action_reset
            )
            states[STATE_CANCELLED] = ControlState(
                STATE_CANCELLED,
                R.state_cancelled,
                R.action_cancel
            )
            states[STATE_SUCCESS] = ReadOnlyState(
                STATE_SUCCESS,
                R.state_success,
            )
            states[STATE_FAILURE] = ReadOnlyState(
                STATE_FAILURE,
                R.state_failure,
            )
            states[STATE_EXECUTING] = ControlState(
                STATE_EXECUTING,
                R.state_executing,
                R.action_execute
            )
            return states
        }

    protected abstract fun executionCode(instance: InstanceDto) : Pair<Boolean, Resource>

    companion object {
        const val STATE_READY = "ready"
        const val STATE_EXECUTING = "executed"
        const val STATE_SUCCESS = "success"
        const val STATE_FAILURE = "failure"
        const val STATE_CANCELLED = "cancelled"
    }
}