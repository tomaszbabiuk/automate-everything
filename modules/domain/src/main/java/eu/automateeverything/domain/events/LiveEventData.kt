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
import eu.automateeverything.data.localization.Language
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.automation.EvaluationResult
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.Port
import kotlinx.serialization.Serializable
import org.pf4j.PluginWrapper


sealed class LiveEventData

@Serializable
class DiscoveryEventData(val factoryId: String,
                         val message: String) : LiveEventData() {
    override fun toString(): String {
        return "Discovery event from $factoryId: ($message)"
    }
}

@Serializable
class PortUpdateEventData(val factoryId: String,
                          val adapterId: String,
                          val port: Port<*>) : LiveEventData() {
    override fun toString(): String {
        return if (port is InputPort<*>) {
            "Update event of port ${port.id}/$adapterId (${port.read().toFormattedString().getValue(Language.EN)})"
        } else {
            "Update event of port ${port.id}/$adapterId"
        }
    }
}

@Serializable
class InstanceUpdateEventData(val instanceDto: InstanceDto) : LiveEventData() {
    override fun toString(): String {
        return "Instance (id: ${instanceDto.id}) update"
    }
}

@Serializable
class AutomationStateEventData(val enabled: Boolean) : LiveEventData() {
    override fun toString(): String {
        return if (enabled) {
            "Automation event (enabled)"
        } else {
            "Automation event (disabled)"
        }
    }
}


class AutomationUpdateEventData(
    val unit: AutomationUnit<*>,
    val instance: InstanceDto,
    val evaluation: EvaluationResult<*>,
) : LiveEventData()

class PluginEventData(val plugin: PluginWrapper) : LiveEventData() {
    override fun toString(): String {
        return "Plugin ${plugin.pluginId} event: (${plugin.pluginState})"
    }
}

@Serializable
class HeartbeatEventData(
    val timestamp: Long,
    val unreadMessagesCount: Int,
    val isAutomationEnabled: Boolean
) : LiveEventData() {

    override fun toString(): String {
        return "Heartbeat at $timestamp (unread: $unreadMessagesCount, automation: $isAutomationEnabled)"
    }
}

class InboxEventData(val inboxItemDto: InboxItemDto) : LiveEventData() {
    override fun toString(): String {
        return "Inbox message $inboxItemDto"
    }
}

