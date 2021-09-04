package eu.automateeverything.coreplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.data.localization.Resource
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class CorePlugin(wrapper: PluginWrapper) : Plugin(wrapper), PluginMetadata {
    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}