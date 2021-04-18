package eu.geekhome

import eu.geekhome.services.configurable.Configurable
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.PluginEventData
import eu.geekhome.services.repository.Repository
import org.pf4j.*

interface PluginsCoordinator {
    fun enablePlugin(id: String): PluginWrapper
    fun disablePlugin(id: String): PluginWrapper
    fun loadPlugins()
    fun startPlugins()
    fun addPluginStateListener(listener: PluginStateListener)
    fun getPlugin(id: String): PluginWrapper

    val plugins: List<PluginWrapper>
    val configurables: List<Configurable>
    val repository: Repository
}

class SingletonExtensionsPluginManager(private val liveEvents: EventsSink) : PluginsCoordinator {

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

    override fun getPlugin(id: String): PluginWrapper {
        return wrapped.getPlugin(id)
    }

    override fun enablePlugin(id: String): PluginWrapper {
        wrapped.enablePlugin(id)
        wrapped.startPlugin(id)
        return getPlugin(id)
    }

    override fun disablePlugin(id: String): PluginWrapper {
        wrapped.stopPlugin(id)
        wrapped.disablePlugin(id)
        return getPlugin(id)
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

    override val repository: Repository
        get() = wrapped.getExtensions(Repository::class.java).first()

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}