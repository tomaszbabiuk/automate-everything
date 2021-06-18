package eu.geekhome.shellyplugin

import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.localization.Resource
import eu.geekhome.domain.mqtt.MqttBrokerPlugin
import eu.geekhome.landiscoverysettings.LanDiscoverySettings
import org.pf4j.PluginState
import org.pf4j.PluginStateEvent
import org.pf4j.PluginStateListener
import org.pf4j.PluginWrapper

class ShellyPlugin(wrapper: PluginWrapper) : HardwarePlugin(wrapper), PluginMetadata, PluginStateListener {

    companion object {
        const val PLUGIN_ID_MQTT = "mqtt"
        const val PLUGIN_ID_SHELLY = "shelly"
    }

    private val factory: ShellyAdapterFactory

    override fun getFactory(): HardwareAdapterFactory {
        return factory
    }

    override fun start() {
        println("Starting SHELLY plugin")
        val manager = wrapper.pluginManager
        val mqttPluginWrapper = manager.getPlugin(PLUGIN_ID_MQTT)
        if (mqttPluginWrapper.pluginState != PluginState.STARTED) {
            manager.startPlugin(PLUGIN_ID_MQTT)
        }
    }

    override fun stop() {
        println("Stopping SHELLY plugin")
    }

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description

    override val settingGroups: List<SettingGroup>
        get() = listOf(LanDiscoverySettings())

    override fun pluginStateChanged(event: PluginStateEvent) {
        if (event.pluginState == PluginState.STOPPED) {
            if (event.plugin.pluginId == PLUGIN_ID_MQTT) {
                println("MQTT plugin stopped, shelly cannot continue... Stopping")
                getWrapper().pluginManager.stopPlugin(PLUGIN_ID_SHELLY)
            }
        }
    }

    init {
        val manager = wrapper.pluginManager
        manager.addPluginStateListener(this)
        val mqttPluginWrapper = manager.getPlugin(PLUGIN_ID_MQTT)
        val broker = (mqttPluginWrapper.plugin as MqttBrokerPlugin).broker
        factory = ShellyAdapterFactory(PLUGIN_ID_SHELLY, broker)
    }
}