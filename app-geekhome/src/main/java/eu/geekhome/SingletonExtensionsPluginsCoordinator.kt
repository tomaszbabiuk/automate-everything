package eu.geekhome

import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PluginEventData
import eu.geekhome.domain.extensibility.PluginMetadata
import org.pf4j.*

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
}

class SingletonExtensionsPluginsCoordinator(
    private val liveEvents: EventsSink) : PluginsCoordinator {

    private val wrapped: JarPluginManager = object : JarPluginManager(), PluginStateListener {
        override fun createExtensionFactory(): ExtensionFactory {
            return SingletonExtensionFactory()
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

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}