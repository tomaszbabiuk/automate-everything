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

package eu.automateeverything.onoffplugin

import eu.automateeverything.data.configurables.ControlType
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.OutputPort
import eu.automateeverything.domain.hardware.PowerLevel
import java.math.BigDecimal

class PowerRegulatorAutomationUnit(
    eventBus: EventBus,
    name: String,
    instanceDto: InstanceDto,
    controlPort: OutputPort<PowerLevel>,
    automationOnly: Boolean,
) : SinglePortRegulatorAutomationUnit<PowerLevel>(eventBus, name, instanceDto, controlPort,
    if (automationOnly) ControlType.NA else ControlType.ControllerOther) {
    override val min: BigDecimal = BigDecimal.ZERO
    override val max: BigDecimal = 100.0.toBigDecimal()
    override val step: BigDecimal = 1.toBigDecimal()
    override val valueClazz: Class<PowerLevel> = PowerLevel::class.java
}

