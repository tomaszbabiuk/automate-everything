package eu.geekhome.domain.mqtt

import org.pf4j.ExtensionPoint

interface MqttBrokerService : ExtensionPoint {
    fun addMqttListener(listener: MqttListener)
    fun removeMqttListener(listener: MqttListener)
    fun publish(topic: String?, content: String)
}