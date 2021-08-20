package eu.geekhome.domain.plugininjection

import eu.geekhome.domain.mqtt.MqttBrokerService

interface RequiresMqtt : AllFeaturesInjectedListener {
    fun injectMqttBrokerService(broker: MqttBrokerService)
}