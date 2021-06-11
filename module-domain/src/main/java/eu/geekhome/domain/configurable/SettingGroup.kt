package eu.geekhome.domain.configurable

import eu.geekhome.domain.localization.Resource

interface SettingGroup {
    val titleRes: Resource
    val descriptionRes: Resource
    val iconRaw: String
    val fieldDefinitions: Map<String, FieldDefinition<*>>
}