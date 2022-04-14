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

package eu.automateeverything.mappers

import eu.automateeverything.R
import eu.automateeverything.data.automationhistory.AutomationHistoryDto
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.events.AutomationStateEventData
import eu.automateeverything.domain.events.AutomationUpdateEventData
import eu.automateeverything.data.localization.Resource
import jakarta.inject.Inject

class AutomationHistoryDtoMapper @Inject constructor() {

    @Throws(MappingException::class)
    fun map(timestamp: Long, event: AutomationUpdateEventData, id: Int): AutomationHistoryDto {
        return AutomationHistoryDto(
            id,
            timestamp,
            Resource.createUniResource(event.instance.fields["name"]!!),
            event.evaluation.interfaceValue,
            event.instance.iconId
        )
    }

    fun map(timestamp: Long, event: AutomationStateEventData, id: Int): AutomationHistoryDto {
        return AutomationHistoryDto(
            id,
            timestamp,
            R.automation_history_automation,
            if (event.enabled) R.automation_history_enabled else R.automation_history_disabled,
            null
        )
    }
}

