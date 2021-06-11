package eu.geekhome.rest.field

import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.domain.configurable.FieldDefinitionDto

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.javaClass.simpleName, field.name, field.hint,
            field.maxSize
        )
    }
}