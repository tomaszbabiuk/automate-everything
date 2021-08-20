package eu.geekhome.data.settings

import eu.geekhome.data.fields.FieldDefinitionDto
import eu.geekhome.data.localization.Resource

data class SettingGroupDto(
    val clazz: String,
    val titleRes: Resource,
    val descriptionRes: Resource,
    val fields: List<FieldDefinitionDto>?,
    val iconRaw: String,
)