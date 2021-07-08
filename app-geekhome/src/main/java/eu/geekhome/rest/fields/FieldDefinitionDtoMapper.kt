package eu.geekhome.rest.fields

import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.data.fields.FieldDefinitionDto

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.javaClass.simpleName, field.name, field.hint,
            field.maxSize
        )
    }
}