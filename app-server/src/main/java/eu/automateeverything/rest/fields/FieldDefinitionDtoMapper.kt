package eu.automateeverything.rest.fields

import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.data.fields.ReferenceDto

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
            },
            field.values
        )
    }
}