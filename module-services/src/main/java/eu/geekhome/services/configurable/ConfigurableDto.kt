package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

data class ConfigurableDto(
    val titleRes: Resource,
    val descriptionRes: Resource,
    val clazz: String,
    val parentClazz: String?,
    val type: ConfigurableType,
    val fields: List<FieldDto>?,
    val addNewRes: Resource?,
    val editRes: Resource?,
    val iconRaw: String,
    val hasAutomation: Boolean,
    val editableIcon: Boolean,
    val taggable: Boolean,
)