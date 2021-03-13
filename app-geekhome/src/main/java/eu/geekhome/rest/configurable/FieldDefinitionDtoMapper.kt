package eu.geekhome.rest.configurable

import eu.geekhome.services.configurable.FieldDefinition
import eu.geekhome.services.configurable.FieldDto

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDto {
        return FieldDto(
            field.javaClass.simpleName, field.name, field.hint,
            field.maxSize
        )
    }
}