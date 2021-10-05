package eu.automateeverything.emailactionplugin

import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.domain.configurable.RequiredStringValidator
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.configurable.StringField

class SMTPSettingGroup : SettingGroup {

    override val titleRes = R.smtp_settings_title
    override val descriptionRes = R.smtp_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_HOST, StringField(FIELD_HOST, R.field_host, 0, FIELD_HOST_IV, RequiredStringValidator())),
        Pair(FIELD_USERNAME, StringField(FIELD_USERNAME, R.field_username, 0, FIELD_HOST_IV, RequiredStringValidator())),
        Pair(FIELD_PASSWORD, StringField(FIELD_PASSWORD, R.field_password, 0, FIELD_HOST_IV, RequiredStringValidator()))
    )

    companion object {
        const val FIELD_HOST = "market_pairs"
        const val FIELD_USERNAME = "username"
        const val FIELD_PASSWORD = "password"
        const val FIELD_HOST_IV = "smtp.gmail.com"
    }
}