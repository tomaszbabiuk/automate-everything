/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.AutomationUnitDto
import eu.automateeverything.data.automation.ValueRangeDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import jakarta.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(unit: AutomationUnit<*>, instance: InstanceDto): AutomationUnitDto {
        return AutomationUnitDto(
            unit.controlType,
            extractValueRange(unit),
            instance,
            evaluationResultDtoMapper.map(unit.lastEvaluation)
        )
    }

    private fun extractValueRange(unit: AutomationUnit<*>): ValueRangeDto? {
        if (unit is ControllerAutomationUnit<*>) {
            return ValueRangeDto(unit.min, unit.max, unit.step)
        }

        return null
    }
}

