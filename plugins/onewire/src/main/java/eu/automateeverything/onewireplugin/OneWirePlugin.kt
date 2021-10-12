package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.mqtt.MqttBrokerService
import org.pf4j.PluginWrapper
import java.util.ArrayList

class OneWirePlugin(
    wrapper: PluginWrapper)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
        println("Starting OneWire plugin")
    }

    override fun stop() {
        println("Stopping OneWire plugin")
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        //val adapter = OneWireAdapter(pluginId)
        //result.add(adapter)
        return result
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
}