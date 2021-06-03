package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

interface SettingGroup {
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val fieldDefinitions: Map<String, FieldDefinition<*>>
}