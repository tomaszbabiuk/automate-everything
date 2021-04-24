package eu.geekhome.services.events

import eu.geekhome.services.automation.DeviceAutomationUnit
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.repository.InstanceDto
import org.pf4j.PluginWrapper

class LiveEvent<T: LiveEventData>(
    val timestamp: Long,
    val number: Int,
    val type: String,
    val data: T
)

sealed class LiveEventData

class DiscoveryEventData(val factoryId: String,
                         val message: String) : LiveEventData()

class PortUpdateEventData(val factoryId: String,
                          val adapterId: String,
                          val port: Port<*>) : LiveEventData()

class AutomationStateEventData(val enabled: Boolean) : LiveEventData()

class AutomationUpdateEventData(
    val unit: DeviceAutomationUnit<*>,
    val instance: InstanceDto
) : LiveEventData()

class PluginEventData(val plugin: PluginWrapper) : LiveEventData()