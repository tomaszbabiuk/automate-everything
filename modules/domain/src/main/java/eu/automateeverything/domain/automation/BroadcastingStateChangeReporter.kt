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
import eu.automateeverything.domain.events.EventsSink

class BroadcastingStateChangeReporter(private val liveEvents: EventsSink) : StateChangeReporter {

    private val listeners = ArrayList<StateChangedListener>()

    override fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto) {
        liveEvents.broadcastAutomationUpdate(deviceUnit, instance, deviceUnit.lastEvaluation)

        listeners.forEach {
            it.onStateChanged(deviceUnit, instance)
        }
    }

    override fun reportDeviceValueChange(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto) {
        liveEvents.broadcastAutomationUpdate(deviceUnit, instance, deviceUnit.lastEvaluation)

        listeners.forEach {
            it.onValueChanged(deviceUnit, instance)
        }
    }

    override fun reportDescriptionsUpdate(deviceUnit: AutomationUnit<*>, instance: InstanceDto) {
        liveEvents.broadcastDescriptionsUpdate(deviceUnit, instance, deviceUnit.lastEvaluation)
    }

    override fun addListener(listener: StateChangedListener) {
        listeners.add(listener)
    }

    override fun removeAllListeners() {
        listeners.clear()
    }
}