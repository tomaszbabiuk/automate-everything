package eu.automateeverything.emailactionplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.emailactionplugin.R
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class EmailActionPlugin(wrapper: PluginWrapper) : Plugin(wrapper), PluginMetadata {
    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}