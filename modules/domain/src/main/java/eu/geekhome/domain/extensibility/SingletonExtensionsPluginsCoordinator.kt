package eu.geekhome.domain.extensibility

import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.events.EventsSink
import eu.geekhome.domain.events.PluginEventData
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.plugininjection.AllFeaturesInjectedListener
import eu.geekhome.domain.plugininjection.RequiresInbox
import eu.geekhome.domain.plugininjection.RequiresLanGatewayResolver
import eu.geekhome.domain.plugininjection.RequiresMqtt
import org.pf4j.*

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

    override fun injectPlugins(
        mqttBrokerService: MqttBrokerService,
        lanGatewayResolver: LanGatewayResolver,
        inbox: Inbox
    ) {
        plugins
            .map { it.plugin }
            .forEach {
                if (it is RequiresLanGatewayResolver) {
                    it.injectLanGatewayResolver(lanGatewayResolver)
                }

                if (it is RequiresMqtt) {
                    it.injectMqttBrokerService(mqttBrokerService)
                }

                if (it is RequiresInbox) {
                    it.injectInbox(inbox)
                }

                if (it is AllFeaturesInjectedListener) {
                    it.allFeaturesInjected()
                }
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
        get() = wrapped.getExtensions(Configurable::class.java)

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}