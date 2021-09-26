package eu.automateeverything.domain.configurable

import eu.automateeverything.data.localization.Resource

interface SettingGroup {
    val titleRes: Resource
    val descriptionRes: Resource
    val fieldDefinitions: Map<String, FieldDefinition<*>>
}