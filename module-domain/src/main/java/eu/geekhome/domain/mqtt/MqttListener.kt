package eu.geekhome.domain.mqtt

import java.net.InetAddress

interface MqttListener {
    fun onPublish(clientID: String, topicName: String, msgAsString: String)
    fun onDisconnected(clientID: String)
    fun onConnected(address: InetAddress)
}