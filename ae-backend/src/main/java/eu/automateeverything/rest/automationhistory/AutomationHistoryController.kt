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

package eu.automateeverything.rest.automationhistory

import eu.automateeverything.data.automationhistory.AutomationHistoryDto
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.mappers.AutomationHistoryDtoMapper
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("automationhistory")
class AutomationHistoryController @Inject constructor(
    private val eventsSink: EventsSink,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getAutomation(): List<AutomationHistoryDto> {
        val states = eventsSink
            .automationStateEvents()
            .map {
                automationHistoryMapper.map(it.timestamp, it.data, it.number)
            }

        val deviceUpdates = eventsSink
            .automationUpdateEvents()
            .map {
                automationHistoryMapper.map(it.timestamp, it.data, it.number)
            }

        return (states + deviceUpdates).sortedBy { it.timestamp }
    }
}