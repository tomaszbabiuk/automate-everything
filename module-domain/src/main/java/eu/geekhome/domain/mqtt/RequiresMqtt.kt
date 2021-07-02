package eu.geekhome.domain.mqtt

interface RequiresMqtt {
    fun injectMqttBrokerService(broker: MqttBrokerService)
}