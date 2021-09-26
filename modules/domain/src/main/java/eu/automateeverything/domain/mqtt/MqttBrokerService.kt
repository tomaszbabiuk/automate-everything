package eu.automateeverything.domain.mqtt

interface MqttBrokerService {
    fun addMqttListener(listener: MqttListener)
    fun removeMqttListener(listener: MqttListener)
    fun publish(topic: String?, content: String)

    fun start()
    fun stop()
}