package eu.automateeverything.domain.extensibility

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SettingGroup
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
    fun getPluginSettingGroups(pluginWrapper: PluginWrapper) : List<SettingGroup>
    fun findExtensionOwner(extensionClazz: Class<*>): PluginWrapper?
    val plugins: List<PluginWrapper>
    val configurables: List<Configurable>
    val blockFactories: List<BlockFactory<*>>
    val blockFactoriesCollectors: List<BlockFactoriesCollector>
}