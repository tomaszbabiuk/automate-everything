package eu.geekhome.shellyplugin

import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwareAdapter
import java.util.ArrayList

internal class ShellyAdapterFactory(
    override val owningPluginId: String,
    private val mqttBroker: MqttBrokerService) :
    HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter> {
        val result = ArrayList<HardwareAdapter>()
        val adapter = ShellyAdapter(owningPluginId, mqttBroker)
        result.add(adapter)
        return result
    }
}