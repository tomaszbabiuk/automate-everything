package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

class FieldDefinitionDto(
    private val clazz: String,
    private val name: String,
    private val hint: Resource,
    private val maxSize: Int
)