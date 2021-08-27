package eu.geekhome.domain.extensibility

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.hardware.PortFinder
import eu.geekhome.domain.inbox.Inbox
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.mqtt.MqttBrokerService
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
    fun injectPlugins(mqttBrokerService: MqttBrokerService, lanGatewayResolver: LanGatewayResolver, inbox: Inbox, portFinder: PortFinder)
    val plugins: List<PluginWrapper>
    val configurables: List<Configurable>
    val blockFactories: List<BlockFactory<*>>
}