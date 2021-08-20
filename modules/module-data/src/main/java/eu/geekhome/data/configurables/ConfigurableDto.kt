package eu.geekhome.data.configurables

import eu.geekhome.data.fields.FieldDefinitionDto
import eu.geekhome.data.localization.Resource

data class ConfigurableDto(
    val titleRes: Resource,
    val descriptionRes: Resource,
    val clazz: String,
    val parentClazz: String?,
    val type: ConfigurableType,
    val fields: List<FieldDefinitionDto>?,
    val addNewRes: Resource?,
    val editRes: Resource?,
    val iconRaw: String,
    val hasAutomation: Boolean,
    val editableIcon: Boolean,
    val taggable: Boolean,
)