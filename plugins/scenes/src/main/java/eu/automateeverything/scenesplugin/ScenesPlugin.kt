package eu.automateeverything.scenesplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class ScenesPlugin(
    wrapper: PluginWrapper) : Plugin(wrapper), PluginMetadata {

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}