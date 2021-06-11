package eu.geekhome.domain.events

import eu.geekhome.domain.automation.DeviceAutomationUnit
import eu.geekhome.domain.automation.EvaluationResult
import eu.geekhome.domain.hardware.Port
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
                         val message: String) : LiveEventData()

class PortUpdateEventData(val factoryId: String,
                          val adapterId: String,
                          val port: Port<*>) : LiveEventData()

class AutomationStateEventData(val enabled: Boolean) : LiveEventData()

class AutomationUpdateEventData(
    val unit: DeviceAutomationUnit<*>,
    val instance: InstanceDto,
    val evaluation: EvaluationResult<*>,
) : LiveEventData()

class PluginEventData(val plugin: PluginWrapper) : LiveEventData()