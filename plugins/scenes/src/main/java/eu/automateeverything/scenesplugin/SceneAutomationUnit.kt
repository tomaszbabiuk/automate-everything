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

package eu.automateeverything.scenesplugin

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.scenesplugin.SceneConfigurable.Companion.STATE_INACTIVE
import java.util.*

class SceneAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    automationOnly: Boolean,
    states: Map<String, State>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name,
        if (automationOnly) { ControlType.NA } else { ControlType.States },
        states, false) {

    init {
        changeState(STATE_INACTIVE)
    }

    @Throws(Exception::class)
    override fun applyNewState(state: String) {
    }

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = false
}