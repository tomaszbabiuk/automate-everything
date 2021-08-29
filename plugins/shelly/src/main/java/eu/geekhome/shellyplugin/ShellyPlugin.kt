package eu.geekhome.shellyplugin

import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.hardware.HardwarePlugin
import eu.geekhome.domain.langateway.LanGatewayResolver
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.mqtt.MqttBrokerService
import org.pf4j.PluginWrapper
import java.util.ArrayList

class ShellyPlugin(
    wrapper: PluginWrapper,
    private val lanGatewayResolver: LanGatewayResolver,
    private val mqttBroker: MqttBrokerService)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
        println("Starting SHELLY plugin")
    }

    override fun stop() {
        println("Stopping SHELLY plugin")
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = ShellyAdapter(pluginId, mqttBroker, lanGatewayResolver)
        result.add(adapter)
        return result
    }

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description
}