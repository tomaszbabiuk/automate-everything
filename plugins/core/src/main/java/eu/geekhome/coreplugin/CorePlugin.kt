package eu.geekhome.coreplugin

import eu.geekhome.services.extensibility.PluginMetadata
import eu.geekhome.services.localization.Resource
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class CorePlugin(wrapper: PluginWrapper?) : Plugin(wrapper), PluginMetadata {
    override fun start() {
        println("WelcomePlugin.start()")
    }

    override fun stop() {
        println("WelcomePlugin.stop()")
    }

    override fun getName(): Resource {
        return R.plugin_name
    }

    override fun getDescription(): Resource {
        return R.plugin_description
    }
}