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
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.hardware.Port
import org.pf4j.PluginWrapper
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class NumberedEventsSink : EventsSink {

    var eventCounter = 0

    private val listeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())

    val events = ArrayList<LiveEvent<*>>()
    val messages = ArrayList<LiveEvent<*>>()

    override fun addEventListener(listener: LiveEventsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LiveEventsListener) {
        listeners.remove(listener)
    }

    private fun enqueue(payload: LiveEventData, target: ArrayList<LiveEvent<*>>, maxSize: Int) {
        if (target.size > maxSize) {
            target.removeAt(0)
        }

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        target.add(event)
        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
    }

    private fun  broadcastEvent(payload: LiveEventData) {
        enqueue(payload, events, MAX_EVENTS_SIZE)
    }

    private fun broadcastMessage(payload: LiveEventData) {
        enqueue(payload, messages, MAX_INBOX_SIZE)
    }

    override fun reset() {
        eventCounter = 0
        events.clear()
    }

    override fun removeRange(filter: Predicate<in LiveEvent<*>>) {
        events.removeIf(filter)
    }

    override fun all(): List<LiveEvent<*>> {
        return events
    }

    override fun broadcastDiscoveryEvent(factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        broadcastEvent(event)
    }

    override fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        broadcastEvent(event)
    }

    override fun broadcastInstanceUpdateEvent(instanceDto: InstanceDto) {
        val event = InstanceUpdateEventData(instanceDto)
        broadcastEvent(event)
    }

    override fun broadcastHeartbeatEvent(timestamp: Long, unreadMessagesCount: Int, isAutomationEnabled: Boolean) {
        val event = HeartbeatEventData(timestamp, unreadMessagesCount, isAutomationEnabled)
        broadcastEvent(event)
    }

    override fun broadcastAutomationUpdate(
        unit: AutomationUnit<*>,
        instance: InstanceDto,
        newEvaluation: EvaluationResult<out Any?>
    ) {
        val eventData = AutomationUpdateEventData(unit, instance, newEvaluation)
        broadcastEvent(eventData)
    }

    override fun broadcastAutomationStateChange(enabled: Boolean) {
        broadcastEvent(AutomationStateEventData(enabled))
    }

    override fun broadcastPluginEvent(plugin: PluginWrapper) {
        val eventData = PluginEventData(plugin)
        broadcastEvent(eventData)

    }

    override fun broadcastInboxMessage(inboxItemDto: InboxItemDto) {
        val event = InboxEventData(inboxItemDto)
        broadcastMessage(event)
    }

    companion object {
        const val MAX_EVENTS_SIZE = 200
        const val MAX_INBOX_SIZE = 100
    }
}