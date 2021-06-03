package eu.geekhome.coreplugin

import eu.geekhome.services.configurable.SettingGroup
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

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val settingGroups: List<SettingGroup>
        get() = listOf(LanDiscoverySettings())
}