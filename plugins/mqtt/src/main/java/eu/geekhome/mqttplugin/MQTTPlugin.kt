package eu.geekhome.mqttplugin

import eu.geekhome.services.extensibility.PluginMetadata
import eu.geekhome.services.localization.Resource
import eu.geekhome.services.mqtt.MqttBrokerPlugin
import eu.geekhome.services.mqtt.MqttBrokerService
import org.pf4j.PluginWrapper

class MQTTPlugin(wrapper: PluginWrapper?) : MqttBrokerPlugin(wrapper), PluginMetadata {

    private var _broker: MoquetteBroker = MoquetteBroker()

    override fun start() {
        println("Starting MQTT plugin")
        _broker.start()
    }

    override fun stop() {
        println("Stopping MQTT plugin")
        _broker.stop()
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description

    override fun getBroker(): MqttBrokerService {
        return _broker
    }
}
