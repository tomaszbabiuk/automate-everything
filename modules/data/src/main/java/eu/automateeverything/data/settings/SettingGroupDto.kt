package eu.automateeverything.data.settings

import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.data.localization.Resource

data class SettingGroupDto(
    val clazz: String,
    val titleRes: Resource,
    val descriptionRes: Resource,
    val fields: List<FieldDefinitionDto>?
)