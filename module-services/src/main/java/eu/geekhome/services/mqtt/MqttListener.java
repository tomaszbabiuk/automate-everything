package eu.geekhome.services.mqtt;

import java.net.InetAddress;

public interface MqttListener {
    void onPublish(String topicName, String msgAsString);

    void onDisconnected(String clientID);

    void onConnected(InetAddress address);
}
