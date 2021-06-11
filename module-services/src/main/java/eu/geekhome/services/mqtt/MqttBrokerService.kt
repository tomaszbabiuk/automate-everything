package eu.geekhome.services.mqtt

import org.pf4j.ExtensionPoint

interface MqttBrokerService : ExtensionPoint {
    fun addMqttListener(listener: MqttListener)
    fun removeMqttListener(listener: MqttListener)
    fun publish(topic: String?, content: String)
}