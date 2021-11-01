package eu.automateeverything.centralheatingplugin

import eu.automateeverything.aforeplugin.R
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.BasicPlugin
import org.pf4j.PluginWrapper

class CentralHeatingPlugin(
    wrapper: PluginWrapper) : BasicPlugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}