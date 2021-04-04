package eu.geekhome.services.events

import eu.geekhome.services.hardware.Port
import org.pf4j.PluginWrapper

class LiveEvent<T: LiveEventData>(
    val number: Int,
    val type: String,
    val data: T
)

sealed class LiveEventData

class HardwareEvent(val factoryId: String,
                    val message: String) : LiveEventData()

class PortUpdateEvent(val factoryId: String,
                      val adapterId: String,
                      val port: Port<*>) : LiveEventData()

class AutomationUpdateEvent(val enabled: Boolean) : LiveEventData()

class PluginEvent(val plugin: PluginWrapper) : LiveEventData()