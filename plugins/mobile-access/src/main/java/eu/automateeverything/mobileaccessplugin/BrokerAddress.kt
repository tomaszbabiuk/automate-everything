package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.data.settings.SettingsDto

class BrokerAddress(pluginSettings: List<SettingsDto>) {

    val host: String
    val user: String
    val password: String

    init {
        if (pluginSettings.size == 2) {
            val mqttSettings = pluginSettings.first { it.clazz == MqttBrokerSettingGroup::class.java.name }
            host = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_ADDRESS]!!
            user = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_USER]!!
            password = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_PASSWORD]!!
        } else {
            host = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_ADDRESS
            user = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_USER
            password = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_PASSWORD
        }
    }
}