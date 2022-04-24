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
import eu.automateeverything.domain.automation.StateChangedListener
import eu.automateeverything.domain.hardware.Port
import org.pf4j.PluginWrapper

interface LiveEventsListener {
    fun onEvent(event: LiveEvent<*>)
}

interface EventBus {
    fun subscribeToGlobalEvents(listener: LiveEventsListener)
    fun unsubscribeFromGlobalEvents(listener: LiveEventsListener)

    val discoveryEvents: List<LiveEvent<DiscoveryEventData>>
    val automationStateEvents: List<LiveEvent<AutomationStateEventData>>
    val automationUpdateEvents: List<LiveEvent<AutomationUpdateEventData>>

    fun broadcastDiscoveryEvent(factoryId: String, message: String)
    fun broadcastPortUpdateEvent(factoryId: String, adapterId: String, port: Port<*>)
    fun broadcastInstanceUpdateEvent(instanceDto: InstanceDto)
    fun broadcastHeartbeatEvent(
        timestamp: Long,
        unreadMessagesCount: Long,
        totalMessagesCount: Long,
        isAutomationEnabled: Boolean
    )
    fun broadcastInboxMessage(inboxItemDto: InboxItemDto)
    fun broadcastAutomationStateChange(enabled: Boolean)
    fun broadcastPluginEvent(plugin: PluginWrapper)
    fun broadcastAutomationUpdate(
        unit: AutomationUnit<*>,
        instance: InstanceDto
    )
    fun broadcastDescriptionsUpdate(
        unit: AutomationUnit<*>,
        instance: InstanceDto
    )


    fun subscribeToStateChanges(listener: StateChangedListener)
    fun unsubscribeFromStateChanges()
}