package eu.geekhome.sqldelightplugin

import eu.geekhome.services.extensibility.PluginMetadata
import eu.geekhome.services.localization.Resource
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class SqlDelightPlugin(wrapper: PluginWrapper?) : Plugin(wrapper), PluginMetadata {
    override fun start() {
        println("Starting SQLDelight plugin")
    }

    override fun stop() {
        println("SQLDelight plugin.stop()")
    }

    override fun getName(): Resource {
        return R.plugin_name
    }

    override fun getDescription(): Resource {
        return R.plugin_description
    }
}
