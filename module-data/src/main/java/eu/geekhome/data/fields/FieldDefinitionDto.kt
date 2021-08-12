package eu.geekhome.data.fields

import eu.geekhome.data.localization.Resource

class FieldDefinitionDto(
    private val clazz: String,
    private val name: String,
    private val hint: Resource,
    private val maxSize: Int,
    private val initialValue: String
)