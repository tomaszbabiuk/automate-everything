package eu.geekhome.domain.mqtt;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class MqttBrokerPlugin extends Plugin {

    public MqttBrokerPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    public abstract MqttBrokerService getBroker();
}
