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

package eu.automateeverything.rest.live

import eu.automateeverything.rest.automation.AutomationUnitDtoMapper
import eu.automateeverything.rest.automationhistory.AutomationHistoryDtoMapper
import eu.automateeverything.rest.hardware.NumberedHardwareEventToEventDtoMapper
import eu.automateeverything.rest.hardware.PortDtoMapper
import eu.automateeverything.rest.plugins.PluginDtoMapper
import eu.automateeverything.domain.events.*
import eu.automateeverything.rest.inbox.InboxMessageDtoMapper
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

@Path("live")
class LiveController @Inject constructor(
    eventsSink: EventsSink,
    private val portDtoMapper: PortDtoMapper,
    private val pluginDtoMapper: PluginDtoMapper,
    private val hardwareEventMapper: NumberedHardwareEventToEventDtoMapper,
    private val automationUnitMapper: AutomationUnitDtoMapper,
    private val automationHistoryMapper: AutomationHistoryDtoMapper,
    private val heartbeatDtoMapper: HeartbeatDtoMapper,
    private val inboxMessageDtoMapper: InboxMessageDtoMapper,
    private val sse: Sse
) {
    private val sseBroadcaster = sse.newBroadcaster()

    init {
        eventsSink.addAdapterEventListener(object : LiveEventsListener {
            override fun onEvent(event: LiveEvent<*>) {
                broadcastLiveEvent(event)
            }
        })
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    fun streamPluginChangesLive(@Context sink: SseEventSink?) {
        sseBroadcaster.register(sink)
    }

    private fun broadcast(clazz: Class<*>, obj: Any) {
        val sseEvent: OutboundSseEvent = sse.newEventBuilder()
            .name(obj.javaClass.simpleName)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(clazz, obj)
            .build()
        sseBroadcaster.broadcast(sseEvent)
    }

    private fun broadcastLiveEvent(event: LiveEvent<*>) {
        when (event.data) {
            is PortUpdateEventData -> {
                val payload = event.data as PortUpdateEventData
                val mapped = portDtoMapper.map(payload.port, payload.factoryId, payload.adapterId)
                broadcast(mapped.javaClass, mapped)
            }
            is PluginEventData -> {
                val payload = event.data as PluginEventData
                val mapped = pluginDtoMapper.map(payload.plugin)
                broadcast(mapped.javaClass, mapped)
            }
            is DiscoveryEventData -> {
                val payload = event.data as DiscoveryEventData
                val mapped = hardwareEventMapper.map(event.number, payload)
                broadcast(mapped.javaClass, mapped)
            }
            is AutomationUpdateEventData -> {
                val payload = event.data as AutomationUpdateEventData
                val mapped = automationUnitMapper.map(payload.unit, payload.instance)
                broadcast(mapped.javaClass, mapped)

                val payloadHistory = event.data as AutomationUpdateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)
                broadcast(mappedHistory.javaClass, mappedHistory)
            }
            is AutomationStateEventData -> {
                val payload = event.data as AutomationStateEventData
                val mapped = payload.enabled
                broadcast(mapped.javaClass, mapped)

                val payloadHistory = event.data as AutomationStateEventData
                val mappedHistory = automationHistoryMapper.map(event.timestamp, payloadHistory, event.number)
                broadcast(mappedHistory.javaClass, mappedHistory)
            }
            is HeartbeatEventData -> {
                val payload = event.data as HeartbeatEventData
                val mapped = heartbeatDtoMapper.map(payload)
                broadcast(mapped.javaClass, mapped)
            }
            is InboxEventData -> {
                val payload = event.data as InboxEventData
                val mapped = inboxMessageDtoMapper.map(payload.inboxItemDto)
                broadcast(mapped.javaClass, mapped)
            }
        }


    }
}