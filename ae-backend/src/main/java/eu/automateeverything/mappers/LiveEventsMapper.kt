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

import eu.automateeverything.data.Mapper
import eu.automateeverything.domain.events.*
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
) : Mapper<LiveEvent<*>, List<Any>> {

    override fun map(from: LiveEvent<*>): List<Any> {
        return when (from.data) {
            is PortUpdateEventData -> {
                val payload = from.data as PortUpdateEventData
                listOf(portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId))
            }
            is PluginEventData -> {
                val payload = from.data as PluginEventData
                listOf(pluginDtoMapper.map(payload.plugin))
            }
            is DiscoveryEventData -> {
                val payload = from.data as DiscoveryEventData
                listOf(hardwareEventMapper.map(from.number, payload))
            }
            is AutomationUpdateEventData -> {
                val payload = from.data as AutomationUpdateEventData
                val mappedUnit = automationUnitMapper.map(payload.unit, payload.instance)

                val payloadHistory = from.data as AutomationUpdateEventData
                val mappedHistory = automationHistoryMapper.map(from.timestamp, payloadHistory, from.number)

                listOf(mappedUnit, mappedHistory)
            }
            is AutomationStateEventData -> {
                val payload = from.data as AutomationStateEventData
                val mappedState = payload.enabled

                val payloadHistory = from.data as AutomationStateEventData
                val mappedHistory = automationHistoryMapper.map(from.timestamp, payloadHistory, from.number)

                listOf(mappedState, mappedHistory)
            }
            is HeartbeatEventData -> {
                val payload = from.data as HeartbeatEventData
                val mapped = heartbeatDtoMapper.map(payload)
                listOf(mapped)
            }
            is InboxEventData -> {
                val payload = from.data as InboxEventData
                val mapped = inboxMessageDtoMapper.map(payload.inboxItemDto)
                listOf(mapped)
            }
            is InstanceUpdateEventData -> {
                val payload = from.data as InstanceUpdateEventData
                val mapped = payload.instanceDto
                listOf(mapped)
            }
            else -> {
                throw NotSupportedException()
            }
        }
    }
}