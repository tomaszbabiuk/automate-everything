package eu.geekhome.domain.extensibility

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SettingGroup
import org.pf4j.PluginStateListener
import org.pf4j.PluginWrapper

interface PluginsCoordinator {
    fun enablePlugin(pluginId: String): PluginWrapper?
    fun disablePlugin(pluginId: String): PluginWrapper?
    fun loadPlugins()
    fun startPlugins()
    fun addPluginStateListener(listener: PluginStateListener)
    fun getPluginWrapper(pluginId: String): PluginWrapper?
    fun getPluginSettingGroups(pluginId: String) : List<SettingGroup>
    val plugins: List<PluginWrapper>
    val configurables: List<Configurable>
    val blockFactories: List<BlockFactory<*>>
    val blockFactoriesCollectors: List<BlockFactoriesCollector>
}