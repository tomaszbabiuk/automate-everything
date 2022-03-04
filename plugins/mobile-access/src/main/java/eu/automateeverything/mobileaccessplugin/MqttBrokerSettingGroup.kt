package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.domain.configurable.*

class MqttBrokerSettingGroup : SettingGroup {
    override val titleRes = R.mqtt_settings_title
    override val descriptionRes = R.mqtt_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_MQTT_BROKER_ADDRESS,
            StringField(
                FIELD_MQTT_BROKER_ADDRESS,
                R.field_mqtt_broker_address,
                0,
                DEFAULT_MQTT_BROKER_ADDRESS,
                RequiredStringValidator()
            )
        ),
        Pair(FIELD_MQTT_BROKER_USER, StringField(FIELD_MQTT_BROKER_USER, R.field_username, 0, DEFAULT_MQTT_BROKER_USER)),
        Pair(FIELD_MQTT_BROKER_PASSWORD,
            PasswordStringField(FIELD_MQTT_BROKER_PASSWORD, R.field_password, 0, DEFAULT_MQTT_BROKER_PASSWORD)
        )

    )

    companion object {
        const val FIELD_MQTT_BROKER_ADDRESS = "broker-address"
        const val DEFAULT_MQTT_BROKER_ADDRESS = "tcp://localhost:1883"
        const val FIELD_MQTT_BROKER_USER = "broker-user"
        const val DEFAULT_MQTT_BROKER_USER = ""
        const val FIELD_MQTT_BROKER_PASSWORD = "broker-password"
        const val DEFAULT_MQTT_BROKER_PASSWORD = ""
    }
}