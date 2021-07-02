package eu.geekhome.domain.mqtt

interface MqttBrokerService {
    fun addMqttListener(listener: MqttListener)
    fun removeMqttListener(listener: MqttListener)
    fun publish(topic: String?, content: String)
}