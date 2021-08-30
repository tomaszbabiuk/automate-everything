package eu.geekhome.domain.extensibility

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.automation.blocks.BlockFactoriesCollector
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PluginEventData
import org.pf4j.*

class SingletonExtensionPluginsCoordinator(
    private val liveEvents: EventsSink,
    private val injectionRegistry: InjectionRegistry
) : PluginsCoordinator {

    private val wrapped: JarPluginManager = object : JarPluginManager(), PluginStateListener {
        override fun createExtensionFactory(): ExtensionFactory {
            return SingletonExtensionFactoryWithDI(injectionRegistry)
        }

        override fun createPluginFactory(): PluginFactory {
            return PluginFactoryWithDI(injectionRegistry)
        }

        init {
            addPluginStateListener(this)
        }

        override fun pluginStateChanged(event: PluginStateEvent) {
            val plugin = event.plugin
            val payload = PluginEventData(plugin)
            liveEvents.broadcastEvent(payload)
        }
    }

    override fun getPluginWrapper(pluginId: String): PluginWrapper? {
        return wrapped.getPlugin(pluginId)
    }

    override fun getPluginSettingGroups(pluginId: String): List<SettingGroup> {
        val pluginWrapper = getPluginWrapper(pluginId) ?: return listOf()
        val metadata = pluginWrapper.plugin as PluginMetadata
        return metadata.settingGroups
    }

    override fun enablePlugin(pluginId: String): PluginWrapper? {
        if (wrapped.enablePlugin(pluginId)) {
            wrapped.startPlugin(pluginId)
            return getPluginWrapper(pluginId)
        }

        return null
    }

    override fun disablePlugin(pluginId: String): PluginWrapper? {
        if (wrapped.disablePlugin(pluginId)) {
            return getPluginWrapper(pluginId)
        }

        return null
    }

    override fun loadPlugins() {
        wrapped.loadPlugins()
    }

    override fun startPlugins() {
        wrapped.startPlugins()
    }

    override fun addPluginStateListener(listener: PluginStateListener) {
        wrapped.addPluginStateListener(listener)
    }

    override val configurables: List<Configurable>
        get() = wrapped.getExtensions(Configurable::class.java)

    override val blockFactories: List<BlockFactory<*>>
        get() = wrapped.getExtensions(BlockFactory::class.java)

    override val blockFactoriesCollectors: List<BlockFactoriesCollector>
        get() = wrapped.getExtensions(BlockFactoriesCollector::class.java)

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}