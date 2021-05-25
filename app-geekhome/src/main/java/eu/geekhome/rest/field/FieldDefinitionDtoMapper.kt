package eu.geekhome.rest.field

import eu.geekhome.services.configurable.FieldDefinition
import eu.geekhome.services.configurable.FieldDefinitionDto

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.javaClass.simpleName, field.name, field.hint,
            field.maxSize
        )
    }
}