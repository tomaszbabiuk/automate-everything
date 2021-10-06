package eu.automateeverything.domain.extensibility

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.events.PluginEventData
import org.pf4j.*


class SingletonExtensionPluginsCoordinator(
    private val liveEvents: EventsSink,
    private val injectionRegistry: InjectionRegistry,
    private val repository: Repository,
) : PluginsCoordinator {

    private val wrapped: JarPluginManager = object : JarPluginManager(), PluginStateListener {
        override fun createExtensionFactory(): ExtensionFactory {
            val extractor = ExtensionSettingsExtractor(this@SingletonExtensionPluginsCoordinator, repository)
            return SingletonExtensionFactoryWithDI(injectionRegistry, extractor)
        }

        override fun createPluginFactory(): PluginFactory {
            return PluginFactoryWithDI(injectionRegistry)
        }

//        override fun createExtensionFinder(): ExtensionFinder {
//            val extensionFinder = super.createExtensionFinder() as DefaultExtensionFinder
//            extensionFinder.addServiceProviderExtensionFinder()
//            return extensionFinder
//        }

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

    override fun getPluginSettingGroups(pluginWrapper: PluginWrapper): List<SettingGroup> {
        val metadata = pluginWrapper.plugin as PluginMetadata
        return metadata.settingGroups
    }

    override fun findExtensionOwner(extensionClazz: Class<*>): PluginWrapper? {
        return wrapped
            .plugins.firstOrNull {
                wrapped
                    .getExtensionClasses(it.pluginId)
                    .contains(extensionClazz)
            }
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
        get()  = wrapped.getExtensions(Configurable::class.java)

    override val blockFactories: List<BlockFactory<*>>
        get() = wrapped.getExtensions(BlockFactory::class.java)

    override val blockFactoriesCollectors: List<BlockFactoriesCollector>
        get() = wrapped.getExtensions(BlockFactoriesCollector::class.java)

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}