package eu.automateeverything.shellyplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.mqtt.MqttBrokerService
import org.pf4j.PluginWrapper
import java.util.ArrayList

class ShellyPlugin(
    wrapper: PluginWrapper,
    private val lanGatewayResolver: LanGatewayResolver,
    private val mqttBroker: MqttBrokerService,
    private val eventsSink: EventsSink)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
    }

    override fun stop() {
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = ShellyAdapter(pluginId, mqttBroker, lanGatewayResolver, eventsSink)
        result.add(adapter)
        return result
    }

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description
}