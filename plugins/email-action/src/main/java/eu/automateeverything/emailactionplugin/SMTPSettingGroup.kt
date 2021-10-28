package eu.automateeverything.emailactionplugin

import eu.automateeverything.domain.configurable.*

class SMTPSettingGroup : SettingGroup {

    override val titleRes = R.smtp_settings_title
    override val descriptionRes = R.smtp_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_HOST, StringField(FIELD_HOST, R.field_host, 0, FIELD_HOST_IV, RequiredStringValidator())),
        Pair(FIELD_USERNAME, StringField(FIELD_USERNAME, R.field_username, 0, "", RequiredStringValidator())),
        Pair(FIELD_PASSWORD, PasswordStringField(FIELD_PASSWORD, R.field_password, 0, "", RequiredStringValidator()))
    )

    companion object {
        const val FIELD_HOST = "host"
        const val FIELD_USERNAME = "username"
        const val FIELD_PASSWORD = "password"
        const val FIELD_HOST_IV = "smtp.gmail.com"
    }
}