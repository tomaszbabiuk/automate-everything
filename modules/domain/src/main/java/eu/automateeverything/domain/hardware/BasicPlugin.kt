package eu.automateeverything.domain.hardware

import org.pf4j.Plugin
import org.pf4j.PluginWrapper

abstract class BasicPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    val pluginId: String = wrapper.pluginId
}