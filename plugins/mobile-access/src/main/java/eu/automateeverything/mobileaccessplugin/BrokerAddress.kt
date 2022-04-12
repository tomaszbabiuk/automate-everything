package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.data.settings.SettingsDto

class BrokerAddress(pluginSettings: List<SettingsDto>) {

    val host: String
    val user: String
    val password: String
    val port: Int
    val tlsRequired: Boolean

    init {
        if (pluginSettings.size == 2) {
            val mqttSettings = pluginSettings.first { it.clazz == MqttBrokerSettingGroup::class.java.name }
            host = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_ADDRESS] ?: ""
            user = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_USER] ?: ""
            password = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_PASSWORD] ?: ""
            port = (mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_PORT] ?: "0").toInt()
            tlsRequired = mqttSettings.fields[MqttBrokerSettingGroup.FIELD_MQTT_BROKER_TLS] == "1"
        } else {
            host = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_ADDRESS
            user = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_USER
            password = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_PASSWORD
            port = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_PORT.toInt()
            tlsRequired = MqttBrokerSettingGroup.DEFAULT_MQTT_BROKER_TLS
        }
    }
}