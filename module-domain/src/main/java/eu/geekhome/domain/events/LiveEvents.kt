package eu.geekhome.domain.events

import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.automation.EvaluationResult
import eu.geekhome.domain.hardware.InputPort
import eu.geekhome.domain.hardware.Port
import eu.geekhome.domain.localization.Language
import eu.geekhome.domain.repository.InstanceDto
import org.pf4j.PluginWrapper

class LiveEvent<T: LiveEventData>(
    val timestamp: Long,
    val number: Int,
    val type: String,
    val data: T
)

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

object LiveEventsHelper {
    fun broadcastDiscoveryEvent(eventsSink: EventsSink, factoryId: String, message: String) {
        val event = DiscoveryEventData(factoryId, message)
        eventsSink.broadcastEvent(event)
    }

    fun broadcastPortUpdateEvent(eventsSink: EventsSink, factoryId: String, adapterId: String, port: Port<*>) {
        val event = PortUpdateEventData(factoryId, adapterId, port)
        eventsSink.broadcastEvent(event)
    }
}