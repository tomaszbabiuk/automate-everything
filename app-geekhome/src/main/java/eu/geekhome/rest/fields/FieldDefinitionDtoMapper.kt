package eu.geekhome.rest.fields

import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.data.fields.FieldDefinitionDto
import eu.geekhome.data.fields.ReferenceDto

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.type,
            field.name,
            field.hint,
            field.maxSize,
            field.initialValueAsString(),
            if (field.reference != null) {
                ReferenceDto(field.reference!!.clazz.name, field.reference!!.type)
            } else {
                null
            }
        )
    }
}