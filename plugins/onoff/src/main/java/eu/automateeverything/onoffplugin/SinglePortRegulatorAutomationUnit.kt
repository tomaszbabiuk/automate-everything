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
import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.hardware.OutputPort
import java.math.BigDecimal
import java.util.*

abstract class SinglePortRegulatorAutomationUnit<V: PortValue>(
    private val stateChangeReporter: StateChangeReporter,
    name: String,
    private val instance: InstanceDto,
    private val controlPort: OutputPort<V>,
    controlType: ControlType
) : AutomationUnitBase<V>(stateChangeReporter, name, instance, controlType), ControllerAutomationUnit<V> {

    override val usedPortsIds: Array<String>
        get() = arrayOf(controlPort.id)

    override fun calculateInternal(now: Calendar) {
        val actualLevel = controlPort.read()
        lastEvaluation = buildEvaluationResult(actualLevel)
        stateChangeReporter.reportDeviceValueChange(this, instance)
    }

    override var lastEvaluation = buildEvaluationResult(controlPort.read())

    private fun buildEvaluationResult(level: V) : EvaluationResult<V> {
        return EvaluationResult(
            interfaceValue = level.toFormattedString(),
            value = level,
            isSignaled = level.asDecimal() > BigDecimal.ZERO,
            descriptions = lastNotes.values.toList()
        )
    }

    override fun control(newValue: V, actor: String?) {
        val actualLevel = controlPort.read()
        if (actualLevel.asDecimal() != newValue.asDecimal()) {
            controlPort.write(newValue)
            lastEvaluation = buildEvaluationResult(newValue)
            stateChangeReporter.reportDeviceValueChange(this, instance)
        }
    }

    override val recalculateOnTimeChange = false
    override val recalculateOnPortUpdate = true
}