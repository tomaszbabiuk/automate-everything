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

import eu.automateeverything.data.instances.InstanceDto
import java.util.*

class StateDeviceTriggerNode(
    context: AutomationContext,
    private val instanceId: Long,
    private val unit: StateDeviceAutomationUnitBase,
    private val observedStateId: String,
    override val next: StatementNode?
) : StatementNodeBase(), StateChangedListener {

    init {
        context.stateChangeReporter.addListener(this)
    }

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (firstLoop && unit.currentState.id == observedStateId) {
            next?.process(now, firstLoop)
        }
    }

    override fun onStateChanged(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto) {
        if (instance.id == instanceId) {
            if (deviceUnit.currentState.id == observedStateId) {
                next?.process(Calendar.getInstance(), false)
            }
        }
    }

    override fun onValueChanged(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto) {
        //not interested
    }
}