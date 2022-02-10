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

package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.automation.StateType
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.Relay

abstract class StateDeviceAutomationUnitBase(
    private val stateChangeReporter: StateChangeReporter,
    private val instance: InstanceDto,
    name: String,
    controlType: ControlType,
    protected val states: Map<String, State>,
    protected val requiresExtendedWidth: Boolean) : AutomationUnitBase<State>(stateChangeReporter, name, instance, controlType), StateDeviceAutomationUnit{

    override var currentState: State = states[STATE_UNKNOWN]!!
        protected set(value) {
            field = value
            changeState(value.id, null)
        }

    protected fun statesExcept(currentState: State, excludedStates: Array<String>): NextStatesDto {
        val nextStates = states
            .map { it.value }
            .filter { it.type != StateType.ReadOnly }
            .filter { it.id !in excludedStates }
        return NextStatesDto(nextStates, currentState.id, requiresExtendedWidth)
    }

    protected fun onlyOneState(stateId: String): NextStatesDto {
        val nextStates = listOf(states[stateId]!!)
        return NextStatesDto(nextStates, currentState.id, requiresExtendedWidth)
    }

    override fun changeState(state: String, actor: String?) {
        if (currentState.id != state) {
            currentState = states[state]!!
            applyNewState(state)
            evaluateAndReportStateChange()
        }
    }

    private fun evaluateAndReportStateChange() {
        lastEvaluation = buildEvaluationResult(currentState.id, states)
        stateChangeReporter.reportDeviceStateChange(this, instance)
    }

    private fun buildEvaluationResult(initialStateId: String, states: Map<String, State>) : EvaluationResult<State> {
        val state = states[initialStateId]!!
        return EvaluationResult(
            interfaceValue = state.name,
            value = state,
            isSignaled = state.isSignaled,
            nextStates = buildNextStates(state),
            descriptions = lastNotes.values.toList()
        )
    }

    open fun buildNextStates(state: State): NextStatesDto {
        val nextStates = states
            .filter { it.value.type != StateType.ReadOnly }
            .map { it.value }
        return NextStatesDto(nextStates, state.id, requiresExtendedWidth)
    }

    abstract fun applyNewState(state: String)

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        protected fun changeRelayStateIfNeeded(
            port: OutputPort<Relay>,
            state: Relay,
            invalidate: Boolean = false
        ) {
            if (invalidate || state.value != port.read().value) {
                port.write(state)
            }
        }
    }

    override var lastEvaluation = buildEvaluationResult(STATE_UNKNOWN, states)
}