package eu.geekhome.domain.configurable

import eu.geekhome.domain.localization.Resource

class FieldDefinitionDto(
    private val clazz: String,
    private val name: String,
    private val hint: Resource,
    private val maxSize: Int
)