package eu.geekhome.shellyplugin

import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.langateway.LanGatewayResolver
import java.util.ArrayList

internal class ShellyAdapterFactory(
    override val owningPluginId: String,
    private val mqttBroker: MqttBrokerService,
    private val lanGatewayResolver: LanGatewayResolver) :
    HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = ShellyAdapter(owningPluginId, mqttBroker, lanGatewayResolver)
        result.add(adapter)
        return result
    }
}