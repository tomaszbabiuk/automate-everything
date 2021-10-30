package eu.automateeverything.onewireplugin

import eu.automateeverything.domain.configurable.*

class DS2408RolesSettingGroup : SettingGroup {

    override val titleRes = R.ds2408_roles_settings_title
    override val descriptionRes = R.ds2408_roles_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_ADDRESSES_OF_RELAYS, StringField(FIELD_ADDRESSES_OF_RELAYS, R.field_relays_ids, 0, "", RequiredStringValidator())),
    )

    companion object {
        const val FIELD_ADDRESSES_OF_RELAYS = "addresses_of_relays"
    }
}