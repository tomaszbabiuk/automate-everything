package eu.geekhome.domain.events

import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.localization.Language
import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.automation.EvaluationResult
import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.Port
import org.pf4j.PluginWrapper

sealed class LiveEventData

class DiscoveryEventData(val factoryId: String,
                         val message: String) : LiveEventData() {
    override fun toString(): String {
        return "Discovery event from $factoryId: ($message)"
    }
}

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
    val unit: DeviceAutomationUnit<*>,
    val instance: InstanceDto,
    val evaluation: EvaluationResult<*>,
) : LiveEventData()

class PluginEventData(val plugin: PluginWrapper) : LiveEventData() {
    override fun toString(): String {
        return "Plugin ${plugin.pluginId} event: (${plugin.pluginState})"
    }
}

class HeartbeatEventData(val timestamp: Long) : LiveEventData() {
    override fun toString(): String {
        return "Heartbeat at $timestamp"
    }
}

class InboxEventData(val inboxItemDto: InboxItemDto) : LiveEventData() {
    override fun toString(): String {
        return "Inbox message $inboxItemDto"
    }
}

