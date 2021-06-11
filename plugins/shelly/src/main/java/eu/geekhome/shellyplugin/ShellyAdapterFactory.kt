package eu.geekhome.shellyplugin

import eu.geekhome.domain.mqtt.MqttBrokerService
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwareAdapter
import java.util.ArrayList

internal class ShellyAdapterFactory(private val mqttBroker: MqttBrokerService) :
    HardwareAdapterFactory {

    override fun createAdapters(): List<HardwareAdapter> {
        val result = ArrayList<HardwareAdapter>()
        val adapter = ShellyAdapter(id, mqttBroker)
        result.add(adapter)
        return result
    }

    override fun getId(): String {
        return ShellyPlugin.PLUGIN_ID_SHELLY
    }
}