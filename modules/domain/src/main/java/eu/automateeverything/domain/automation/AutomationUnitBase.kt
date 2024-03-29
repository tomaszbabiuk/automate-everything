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
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import eu.automateeverything.domain.events.EventBus
import java.util.*

abstract class AutomationUnitBase<T>(
    private val eventBus: EventBus,
    override val name: String,
    private val instance: InstanceDto,
    override val controlType: ControlType,
    initialEvaluation: EvaluationResult<T>
    ) : AutomationUnit<T> {

    final override var lastEvaluation: EvaluationResult<T> = initialEvaluation
        private set

    abstract override val usedPortsIds: Array<String>
    abstract override val recalculateOnTimeChange: Boolean
    abstract override val recalculateOnPortUpdate: Boolean

    abstract fun calculateInternal(now: Calendar)

    override fun modifyNote(noteId: String, note: Resource) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes[noteId] = note
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
            evaluateAndReportStateUpdate()
        }
    }

    protected var lastNotes: MutableMap<String, Resource> = HashMap()

    override fun removeNote(noteId: String) {
        val lastHashCode = lastNotes.hashCode()
        lastNotes.remove(noteId)
        val newHashCode = lastNotes.hashCode()

        if (lastHashCode != newHashCode) {
            evaluateAndReportStateUpdate()
        }
    }

    private fun evaluateAndReportStateUpdate() {
        lastEvaluation.descriptions = lastNotes.values.toList()
        eventBus.broadcastDescriptionsUpdate(this, instance)
    }

    override fun calculate(now: Calendar) {
        return try {
            calculateInternal(now)
        } catch (ex: Exception) {
            val aex = AutomationErrorException(R.error_automation, ex)
            proposeNewEvaluation(evaluateAsAutomationError(aex))
        }
    }

    override fun markExternalError(ex: AutomationErrorException) {
        proposeNewEvaluation(evaluateAsAutomationError(ex))
    }

    private fun evaluateAsAutomationError(ex: AutomationErrorException): EvaluationResult<T> {
        return EvaluationResult(
            interfaceValue = R.error_automation,
            error = ex,
            descriptions = listOf(ex.localizedMessage)
        )
    }

    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, AutomationUnit<*>>>) {
    }

    override fun proposeNewEvaluation(proposed: EvaluationResult<T>) {
        val current = lastEvaluation
        if (current.value != proposed.value) {
            lastEvaluation = proposed
            eventBus.broadcastAutomationUpdate(this, instance)
        }
    }

    companion object {
        fun <T: PortValue> buildEvaluationResult(value: T) : EvaluationResult<T> {
            return EvaluationResult(
                interfaceValue = value.toFormattedString(),
                value = value,
            )
        }
    }
}