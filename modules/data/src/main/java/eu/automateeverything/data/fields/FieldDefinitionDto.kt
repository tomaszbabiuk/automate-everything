package eu.automateeverything.data.fields

import eu.automateeverything.data.localization.Resource

class FieldDefinitionDto(
    val type: FieldType,
    val name: String,
    val hint: Resource,
    val maxSize: Int,
    val initialValue: String,
    val ref: ReferenceDto?
)

