package eu.automateeverything.alarmplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.hardware.BasicPlugin

class AlarmPlugin(
    wrapper: PluginWrapper) : BasicPlugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}