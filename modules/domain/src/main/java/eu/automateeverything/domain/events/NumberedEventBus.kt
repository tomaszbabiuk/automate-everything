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
import eu.automateeverything.domain.automation.ControllerAutomationUnit
import eu.automateeverything.domain.automation.StateDeviceAutomationUnit
import eu.automateeverything.domain.hardware.Port
import org.pf4j.PluginWrapper
import java.util.*

class NumberedEventBus : EventBus {

    private var eventCounter = 0

    private val eventsListeners = Collections.synchronizedList(ArrayList<LiveEventsListener>())
    private val stateListeners = ArrayList<StateChangedListener>()

    override val automationStateEvents = ArrayList<LiveEvent<AutomationStateEventData>>()
    override val automationUpdateEvents = ArrayList<LiveEvent<AutomationUpdateEventData>>()
    override val discoveryEvents = ArrayList<LiveEvent<DiscoveryEventData>>()

    private val heartbeatEvents = ArrayList<LiveEvent<HeartbeatEventData>>()
    private val inboxEvents = ArrayList<LiveEvent<InboxEventData>>()
    private val instanceUpdateEvents = ArrayList<LiveEvent<InstanceUpdateEventData>>()
    private val pluginEvents = ArrayList<LiveEvent<PluginEventData>>()
    private val portUpdateEvents = ArrayList<LiveEvent<PortUpdateEventData>>()

    override fun subscribeToGlobalEvents(listener: LiveEventsListener) {
        eventsListeners.add(listener)
    }

    override fun unsubscribeFromGlobalEvents(listener: LiveEventsListener) {
        eventsListeners.remove(listener)
    }

    private fun <T: LiveEventData> add(payload: T, target: MutableList<LiveEvent<T>>?) {
        if (target != null && target.size > MAX_EVENTS_SIZE_IN_CATEGORY) {
            target.removeAt(0)
        }

        eventCounter++
        val now = Calendar.getInstance().timeInMillis
        val event: LiveEvent<T> = LiveEvent(now, eventCounter, payload.javaClass.simpleName, payload)
        target?.add(event)

        eventsListeners.filterNotNull().forEach { listener -> listener.onEvent(event) }
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

    override fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, type: PortUpdateType, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, type, port)
        broadcastEvent(event)

        stateListeners.forEach {
            it.onPortUpdate(type, port)
        }
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
        instance: InstanceDto
    ) {
        val eventData = AutomationUpdateEventData(unit, instance, unit.lastEvaluation)
        broadcastEvent(eventData)

        if (unit is ControllerAutomationUnit<*>) {
            stateListeners.forEach {
                it.onValueChanged(unit, instance)
            }
        } else if (unit is StateDeviceAutomationUnit) {
            stateListeners.forEach {
                it.onStateChanged(unit, instance)
            }
        }

    }

    override fun broadcastDescriptionsUpdate(
        unit: AutomationUnit<*>,
        instance: InstanceDto
    ) {
        val eventData = DescriptionsUpdateEventData(instance.id, unit.lastEvaluation.descriptions)
        broadcastEvent(eventData)
    }

    override fun subscribeToStateChanges(listener: StateChangedListener) {
        stateListeners.add(listener)
    }

    override fun unsubscribeFromStateChanges() {
        stateListeners.clear()
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
        broadcastEvent(event)
    }

    companion object {
        const val MAX_EVENTS_SIZE_IN_CATEGORY = 200
    }
}