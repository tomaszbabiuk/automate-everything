package eu.geekhome.data.fields

import eu.geekhome.data.localization.Resource

class FieldDefinitionDto(
    val type: FieldType,
    val name: String,
    val hint: Resource,
    val maxSize: Int,
    val initialValue: String,
    val ref: ReferenceDto?
)

