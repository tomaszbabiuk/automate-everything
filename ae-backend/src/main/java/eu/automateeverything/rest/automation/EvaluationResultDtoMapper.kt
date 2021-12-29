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

import eu.automateeverything.data.automation.EvaluationResultDto
import eu.automateeverything.data.hardware.PortValue
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.EvaluationResult
import java.math.BigDecimal

class EvaluationResultDtoMapper {

    @Throws(MappingException::class)
    fun map(source: EvaluationResult<*>): EvaluationResultDto {
        return EvaluationResultDto(
            source.interfaceValue,
            extractBigDecimalValueIfPossible(source),
            source.isSignaled,
            source.descriptions.flatMap { it.split(System.lineSeparator()) },
            if (source.error != null) {
                source.error!!.toString()
            } else {
                null
            },
            source.nextStates
        )
    }

    private fun extractBigDecimalValueIfPossible(source: EvaluationResult<*>): BigDecimal? {
        if (source.value != null && source.value is PortValue) {
            return (source.value as PortValue).asDecimal()
        }

        return null
    }
}

