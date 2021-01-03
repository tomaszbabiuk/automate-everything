package eu.geekhome.mqttplugin

import com.geekhome.common.extensibility.PluginMetadata
import com.geekhome.common.localization.Resource
import eu.geekhome.services.mqtt.MqttBrokerPlugin
import eu.geekhome.services.mqtt.MqttBrokerService
import org.pf4j.Plugin
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

    override fun getName(): Resource {
        return R.plugin_name
    }

    override fun getDescription(): Resource {
        return R.plugin_description
    }

    override fun getBroker(): MqttBrokerService {
        return _broker;
    }
}
