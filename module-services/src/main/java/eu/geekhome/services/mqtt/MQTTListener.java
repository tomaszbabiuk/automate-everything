package eu.geekhome.services.mqtt;

public interface MQTTListener {
    void onPublish(String topicName, String msgAsString);

    void onDisconnected(String clientID);
}
