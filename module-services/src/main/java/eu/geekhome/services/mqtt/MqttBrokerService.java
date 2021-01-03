package eu.geekhome.services.mqtt;

import org.pf4j.ExtensionPoint;

public interface MqttBrokerService extends ExtensionPoint {
    void addMqttListener(MQTTListener listener);

    void removeMqttListener(MQTTListener listener);

    void publish(String topic, String content);
}
