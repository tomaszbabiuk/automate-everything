package eu.geekhome.shellyplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.domain.plugininjection.RequiresLanGatewayResolver
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.plugininjection.RequiresMqtt
import org.pf4j.PluginWrapper
import java.util.ArrayList

class ShellyPlugin(wrapper: PluginWrapper) : HardwarePlugin(wrapper), PluginMetadata, RequiresMqtt,
    RequiresLanGatewayResolver {

    companion object {
        const val PLUGIN_ID_SHELLY = "shelly"
    }

    private lateinit var lanGatewayResolver: LanGatewayResolver
    private lateinit var mqttBroker: MqttBrokerService

    override fun start() {
        println("Starting SHELLY plugin")
    }

    override fun stop() {
        println("Stopping SHELLY plugin")
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = ShellyAdapter(owningPluginId, mqttBroker, lanGatewayResolver)
        result.add(adapter)
        return result
    }

    override val owningPluginId: String
        get() = PLUGIN_ID_SHELLY

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description

    override fun injectMqttBrokerService(broker: MqttBrokerService) {
        mqttBroker = broker
    }

    override fun injectLanGatewayResolver(resolver: LanGatewayResolver) {
        lanGatewayResolver = resolver
    }

    override fun allFeaturesInjected() {
    }
}