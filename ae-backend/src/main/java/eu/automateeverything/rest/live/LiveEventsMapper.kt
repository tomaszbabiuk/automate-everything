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

package eu.automateeverything.rest.live

import eu.automateeverything.domain.events.*
import eu.automateeverything.rest.automation.AutomationUnitDtoMapper
import eu.automateeverything.rest.automationhistory.AutomationHistoryDtoMapper
import eu.automateeverything.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.automateeverything.rest.hardware.PortDtoMapper
import eu.automateeverything.rest.inbox.InboxMessageDtoMapper
import eu.automateeverything.rest.plugins.PluginDtoMapper
import jakarta.inject.Inject
import jakarta.ws.rs.NotSupportedException

class LiveEventsMapper @Inject constructor(
    private val portDtoMapper: PortDtoMapper,
    private val pluginDtoMapper: PluginDtoMapper,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val automationUnitMapper: AutomationUnitDtoMapper,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
    private val heartbeatDtoMapper: HeartbeatDtoMapper,
    private val inboxMessageDtoMapper: InboxMessageDtoMapper
) {

    fun map(event: LiveEvent<*>): List<Any> {
        return when (event.data) {
            is PortUpdateEventData -> {
                val payload = event.data as PortUpdateEventData
                listOf(portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId))
            }
            is PluginEventData -> {
                val payload = event.data as PluginEventData
                listOf(pluginDtoMapper.map(payload.plugin))
            }
            is DiscoveryEventData -> {
                val payload = event.data as DiscoveryEventData
                listOf(hardwareEventMapper.map(event.number, payload))
            }
            is AutomationUpdateEventData -> {
                val payload = event.data as AutomationUpdateEventData
                val mappedUnit = automationUnitMapper.map(payload.unit, payload.instance)

                val payloadHistory = event.data as AutomationUpdateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)

                listOf(mappedUnit, mappedHistory)
            }
            is AutomationStateEventData -> {
                val payload = event.data as AutomationStateEventData
                val mappedState = payload.enabled

                val payloadHistory = event.data as AutomationStateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)

                listOf(mappedState, mappedHistory)
            }
            is HeartbeatEventData -> {
                val payload = event.data as HeartbeatEventData
                val mapped = heartbeatDtoMapper.map(payload)
                listOf(mapped)
            }
            is InboxEventData -> {
                val payload = event.data as InboxEventData
                val mapped = inboxMessageDtoMapper.map(payload.inboxItemDto)
                listOf(mapped)
            }
            is InstanceUpdateEventData -> {
                val payload = event.data as InstanceUpdateEventData
                val mapped = payload.instanceDto
                listOf(mapped)
            }
            else -> {
                throw NotSupportedException()
            }
        }
    }
}