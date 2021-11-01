package eu.automateeverything.domain.hardware

import org.pf4j.Plugin
import org.pf4j.PluginWrapper

abstract class HardwarePlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    abstract fun createAdapters(): List<HardwareAdapter<*>>
    val pluginId: String = wrapper.pluginId
}

