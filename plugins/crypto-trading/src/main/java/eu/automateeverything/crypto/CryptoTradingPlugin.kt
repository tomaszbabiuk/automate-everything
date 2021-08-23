package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.extensibility.PluginMetadata
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class CryptoTradingPlugin(wrapper: PluginWrapper): Plugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val settingGroups: List<SettingGroup> = listOf(
        BinanceApiSettingGroup()
    )
}