package eu.geekhome.services.mqtt;

public interface MqttBrokerService {
    void addMqttListener(MqttListener listener);

    void removeMqttListener(MqttListener listener);

    void publish(String topic, String content);
}
