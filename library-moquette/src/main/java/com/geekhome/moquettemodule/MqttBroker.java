package com.geekhome.moquettemodule;

public interface MqttBroker {
    void addMqttListener(MqttListener listener);

    void removeMqttListener(MqttListener listener);

    void publish(String topic, String content);
}
