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

package eu.automateeverything.domain.events

import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.hardware.Port
import java.util.function.Predicate

interface LiveEventsListener {
    fun onEvent(event: LiveEvent<*>)
}

interface EventsSink {
    fun addAdapterEventListener(listener: LiveEventsListener)
    fun removeListener(listener: LiveEventsListener)
    fun broadcastEvent(payload: LiveEventData)
    fun broadcastMessage(payload: LiveEventData)
    fun reset()
    fun all() : List<LiveEvent<*>>
    fun removeRange(filter: Predicate<in LiveEvent<*>>)

    fun broadcastDiscoveryEvent(factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        broadcastEvent(event)
    }

    fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        broadcastEvent(event)
    }

    fun broadcastInstanceUpdateEvent(instanceDto: InstanceDto) {
        val event = InstanceUpdateEventData(instanceDto)
        broadcastEvent(event)
    }

    fun broadcastHeartbeatEvent(timestamp: Long, unreadMessagesCount: Int, isAutomationEnabled: Boolean) {
        val event = HeartbeatEventData(timestamp, unreadMessagesCount, isAutomationEnabled)
        broadcastEvent(event)
    }

    fun broadcastInboxMessage(inboxItemDto: InboxItemDto) {
        val event = InboxEventData(inboxItemDto)
        broadcastMessage(event)
    }
}