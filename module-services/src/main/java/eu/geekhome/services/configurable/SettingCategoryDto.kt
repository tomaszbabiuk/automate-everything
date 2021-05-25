package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

data class SettingCategoryDto(
    val clazz: String,
    val titleRes: Resource,
    val descriptionRes: Resource,
    val fields: List<FieldDefinitionDto>?,
    val iconRaw: String,
)