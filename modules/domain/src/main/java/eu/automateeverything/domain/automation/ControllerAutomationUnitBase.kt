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
import java.math.BigDecimal
import java.util.*

open class ControllerAutomationUnitBase<V : PortValue>(
    override val valueClazz: Class<V>,
    stateChangeReporter: StateChangeReporter,
    override val name: String,
    instance: InstanceDto,
    automationOnly: Boolean,
    override val min: BigDecimal,
    override val max: BigDecimal,
    override val step: BigDecimal,
    default: V,
) : AutomationUnitBase<V>(stateChangeReporter, name, instance, if (automationOnly) ControlType.NA else ControlType.ControllerOther, buildEvaluationResult(default)),
    ControllerAutomationUnit<V> {

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true

    override fun control(newValue: V, actor: String?) {
        proposeNewEvaluation(buildEvaluationResult(newValue))
    }

    private fun buildEvaluationResult(level: V) : EvaluationResult<V> {
        return EvaluationResult(
            interfaceValue = level.toFormattedString(),
            value = level,
            descriptions = lastNotes.values.toList()
        )
    }

    override val usedPortsIds: Array<String> = arrayOf()

    override fun calculateInternal(now: Calendar) {
    }
}