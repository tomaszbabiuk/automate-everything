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

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.Port
import java.util.*

class SensorAutomationUnit<T : PortValue>(
    eventBus: EventBus,
    instance: InstanceDto,
    name: String,
    private val port: Port<T>
) :
    AutomationUnitBase<T>(
        eventBus,
        name,
        instance,
        ControlType.NA,
        buildEvaluationResult(port.read())
    ) {

    override val usedPortsIds: Array<String>
        get() = arrayOf(port.portId)

    override fun calculateInternal(now: Calendar) {
        val value = port.read()
        proposeNewEvaluation(buildEvaluationResult(value))
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}
