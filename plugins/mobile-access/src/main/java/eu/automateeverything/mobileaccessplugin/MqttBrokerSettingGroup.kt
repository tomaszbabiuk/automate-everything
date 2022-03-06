package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.domain.configurable.*
import java.math.BigDecimal

class MqttBrokerSettingGroup : SettingGroup {
    override val titleRes = R.mqtt_settings_title
    override val descriptionRes = R.mqtt_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_MQTT_BROKER_ADDRESS, StringField(FIELD_MQTT_BROKER_ADDRESS, R.field_mqtt_broker_address, 0,
                DEFAULT_MQTT_BROKER_ADDRESS,
                RequiredStringValidator())),
        Pair(FIELD_MQTT_BROKER_PORT, NullableBigDecimalField(FIELD_MQTT_BROKER_PORT, R.field_port, BigDecimal(DEFAULT_MQTT_BROKER_PORT), RequiredBigDecimalValidator(), IsIntegerValidator())),
        Pair(FIELD_MQTT_BROKER_USER, StringField(FIELD_MQTT_BROKER_USER, R.field_username, 0,
            DEFAULT_MQTT_BROKER_USER)),
        Pair(FIELD_MQTT_BROKER_PASSWORD, PasswordStringField(FIELD_MQTT_BROKER_PASSWORD, R.field_password, 0,
            DEFAULT_MQTT_BROKER_PASSWORD)),
        Pair(FIELD_MQTT_BROKER_TLS, BooleanField(
            FIELD_MQTT_BROKER_TLS, R.field_tls,
            DEFAULT_MQTT_BROKER_TLS))
    )

    companion object {
        const val FIELD_MQTT_BROKER_ADDRESS = "broker-address"
        const val DEFAULT_MQTT_BROKER_ADDRESS = "localhost"
        const val FIELD_MQTT_BROKER_PORT = "broker-port"
        const val DEFAULT_MQTT_BROKER_PORT = 1883L
        const val FIELD_MQTT_BROKER_USER = "broker-user"
        const val DEFAULT_MQTT_BROKER_USER = ""
        const val FIELD_MQTT_BROKER_PASSWORD = "broker-password"
        const val DEFAULT_MQTT_BROKER_PASSWORD = ""
        const val FIELD_MQTT_BROKER_TLS = "broker-tls"
        const val DEFAULT_MQTT_BROKER_TLS = false
    }
}