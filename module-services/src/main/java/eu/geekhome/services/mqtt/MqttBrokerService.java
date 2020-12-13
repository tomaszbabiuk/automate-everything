package eu.geekhome.services.mqtt;

import org.pf4j.ExtensionPoint;

public interface MqttBrokerService extends ExtensionPoint {
    void addMqttListener(MqttListener listener);

    void removeMqttListener(MqttListener listener);

    void publish(String topic, String content);
}