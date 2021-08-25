package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

interface SettingGroup {
    val titleRes: Resource
    val descriptionRes: Resource
    val fieldDefinitions: Map<String, FieldDefinition<*>>
}