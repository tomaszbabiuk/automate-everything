package eu.geekhome.services.mqtt;

public interface MqttListener {
    void onPublish(String topicName, String msgAsString);

    void onDisconnected(String clientID);
}
