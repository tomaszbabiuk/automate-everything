package eu.geekhome.shellyplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.localization.Resource
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.mqtt.RequiresMqtt
import org.pf4j.PluginWrapper

class ShellyPlugin(wrapper: PluginWrapper) : HardwarePlugin(wrapper),
    PluginMetadata,
    RequiresMqtt{

    companion object {
        const val PLUGIN_ID_SHELLY = "shelly"
    }

    private lateinit var factory: ShellyAdapterFactory

    override fun getFactory(): HardwareAdapterFactory {
        return factory
    }

    override fun start() {
        println("Starting SHELLY plugin")
    }

    override fun stop() {
        println("Stopping SHELLY plugin")
    }

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description

    override fun injectMqttBrokerService(broker: MqttBrokerService) {
        println("Injecting mqtt broker")
        factory = ShellyAdapterFactory(PLUGIN_ID_SHELLY, broker)
    }
}