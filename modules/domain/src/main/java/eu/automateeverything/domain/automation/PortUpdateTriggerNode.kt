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
import eu.automateeverything.domain.events.PortUpdateType
import eu.automateeverything.domain.events.StateChangedListener
import eu.automateeverything.domain.hardware.Port
import java.util.*

class PortUpdateTriggerNode(
    context: AutomationContext,
    private val portId: String,
    override val next: StatementNode?
) : StatementNodeBase(), StateChangedListener {

    init {
        context.eventBus.subscribeToStateChanges(this)
    }

    override fun process(now: Calendar, firstLoop: Boolean) {
        // not interested
    }

    override fun onStateChanged(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto) {
        // not interested
    }

    override fun onValueChanged(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto) {
        // not interested
    }

    override fun onPortUpdate(type: PortUpdateType, port: Port<*>) {
        if (type == PortUpdateType.ValueChange && port.portId == portId) {
            next?.process(Calendar.getInstance(), false)
        }
    }
}
