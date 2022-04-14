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

package eu.automateeverything.actions

import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_CANCELLED
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_EXECUTING
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_FAILURE
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_READY
import eu.automateeverything.actions.ActionConfigurableBase.Companion.STATE_SUCCESS
import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

class ActionAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val executionCode: () -> Pair<Boolean,Resource>
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, ControlType.States, states, false) {

    private var executionScope: CoroutineScope? = null

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
        if (state == STATE_CANCELLED) {
            executionScope?.cancel("Cancelled...")
        }

        if (state == STATE_EXECUTING) {
            executionScope?.cancel("New execution, previous must have been cancelled...")
            executionScope = CoroutineScope(Dispatchers.IO)
            executionScope!!.launch {
                try {
                    val result = executionCode()
                    if (isActive) {
                        modifyNote(EVALUATION_OUTPUT, result.second)
                        if (result.first) {
                            changeState(STATE_SUCCESS)
                        } else {
                            changeState(STATE_FAILURE)
                        }
                    }
                } catch (ex: Exception) {
                    modifyNote(EVALUATION_OUTPUT, Resource.createUniResource(ex.message!!))
                    changeState(STATE_FAILURE)
                }
            }
        }

        if (state == STATE_READY) {
            removeNote(EVALUATION_OUTPUT)
        }
    }


    override fun buildNextStates(state: State): NextStatesDto {
        return when (state.id) {
            STATE_EXECUTING -> {
                onlyOneState(STATE_CANCELLED)
            }
            STATE_READY -> {
                onlyOneState(STATE_EXECUTING)
            }
            else -> {
                onlyOneState(STATE_READY)
            }
        }
    }

    override val usedPortsIds: Array<String> = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
    override val controlType = ControlType.States

    companion object {
        const val EVALUATION_OUTPUT = "output"
    }

    init {
        changeState(STATE_READY)
    }
}