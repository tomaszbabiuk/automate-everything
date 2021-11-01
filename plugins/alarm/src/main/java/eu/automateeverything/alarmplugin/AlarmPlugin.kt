package eu.automateeverything.alarmplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.automateeverything.data.localization.Resource
import org.pf4j.Plugin

class AlarmPlugin(
    wrapper: PluginWrapper) : Plugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}