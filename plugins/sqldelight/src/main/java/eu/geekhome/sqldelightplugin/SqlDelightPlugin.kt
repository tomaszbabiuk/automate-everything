package eu.geekhome.sqldelightplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.localization.Resource
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class SqlDelightPlugin(wrapper: PluginWrapper?) : Plugin(wrapper), PluginMetadata {
    override fun start() {
        println("Starting SQLDelight plugin")
    }

    override fun stop() {
        println("SQLDelight plugin.stop()")
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}
