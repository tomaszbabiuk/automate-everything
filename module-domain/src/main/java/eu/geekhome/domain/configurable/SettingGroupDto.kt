package eu.geekhome.domain.configurable

import eu.geekhome.domain.localization.Resource

data class SettingGroupDto(
    val clazz: String,
    val titleRes: Resource,
    val descriptionRes: Resource,
    val fields: List<FieldDefinitionDto>?,
    val iconRaw: String,
)