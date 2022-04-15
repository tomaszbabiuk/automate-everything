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

package eu.automateeverything.domain.events

import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.hardware.Port
import org.pf4j.PluginWrapper
import java.util.*

class NumberedEventsSink : EventsSink {

    var eventCounter = 0

    private val listeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())

    private val automationStateEvents = ArrayList<LiveEvent<AutomationStateEventData>>()
    private val automationUpdateEvents = ArrayList<LiveEvent<AutomationUpdateEventData>>()
    private val discoveryEvents = ArrayList<LiveEvent<DiscoveryEventData>>()
    private val heartbeatEvents = ArrayList<LiveEvent<HeartbeatEventData>>()
    private val inboxEvents = ArrayList<LiveEvent<InboxEventData>>()
    private val instanceUpdateEvents = ArrayList<LiveEvent<InstanceUpdateEventData>>()
    private val pluginEvents = ArrayList<LiveEvent<PluginEventData>>()
    private val portUpdateEvents = ArrayList<LiveEvent<PortUpdateEventData>>()

    override fun addEventListener(listener: LiveEventsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LiveEventsListener) {
        listeners.remove(listener)
    }

    private fun <T: LiveEventData> add(payload: T, target: MutableList<LiveEvent<T>>?) {
        if (target != null && target.size > MAX_EVENTS_SIZE_IN_CATEGORY) {
            target.removeAt(0)
        }

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event: LiveEvent<T> = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        target?.add(event)

        listeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
    }

    private fun broadcastEvent(payload: LiveEventData) {
        when (payload) {
            is AutomationStateEventData -> add(payload, automationStateEvents)
            is AutomationUpdateEventData -> add(payload, automationUpdateEvents)
            is DiscoveryEventData -> add(payload, discoveryEvents)
            is HeartbeatEventData -> add(payload, heartbeatEvents)
            is InboxEventData -> add(payload, inboxEvents)
            is InstanceUpdateEventData -> add(payload, instanceUpdateEvents)
            is PluginEventData -> add(payload, pluginEvents)
            is PortUpdateEventData -> add(payload, portUpdateEvents)
            is DescriptionsUpdateEventData -> add(payload, null)
        }
    }

    override fun broadcastDiscoveryEvent(factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        broadcastEvent(event)
    }

    override fun discoveryEvents(): List<LiveEvent<DiscoveryEventData>> {
        return discoveryEvents
    }

    override fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        broadcastEvent(event)
    }

    override fun broadcastInstanceUpdateEvent(instanceDto: InstanceDto) {
        val event = InstanceUpdateEventData(instanceDto)
        broadcastEvent(event)
    }

    override fun broadcastHeartbeatEvent(
        timestamp: Long,
        unreadMessagesCount: Long,
        totalMessagesCount: Long,
        isAutomationEnabled: Boolean
    ) {
        val event = HeartbeatEventData(timestamp, unreadMessagesCount, totalMessagesCount, isAutomationEnabled)
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

    override fun broadcastDescriptionsUpdate(
        unit: AutomationUnit<*>,
        instance: InstanceDto,
        newEvaluation: EvaluationResult<out Any?>
    ) {
        val eventData = DescriptionsUpdateEventData(instance.id, newEvaluation.descriptions)
        broadcastEvent(eventData)
    }

    override fun automationUpdateEvents(): List<LiveEvent<AutomationUpdateEventData>> {
        return automationUpdateEvents
    }

    override fun broadcastAutomationStateChange(enabled: Boolean) {
        broadcastEvent(AutomationStateEventData(enabled))
    }

    override fun automationStateEvents(): List<LiveEvent<AutomationStateEventData>> {
        return automationStateEvents
    }

    override fun broadcastPluginEvent(plugin: PluginWrapper) {
        val eventData = PluginEventData(plugin)
        broadcastEvent(eventData)

    }

    override fun broadcastInboxMessage(inboxItemDto: InboxItemDto) {
        val event = InboxEventData(inboxItemDto)
        broadcastEvent(event)
    }

    companion object {
        const val MAX_EVENTS_SIZE_IN_CATEGORY = 200
    }
}