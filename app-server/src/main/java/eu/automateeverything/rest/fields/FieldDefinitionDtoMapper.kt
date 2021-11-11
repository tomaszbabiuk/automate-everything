package eu.automateeverything.rest.fields

import eu.automateeverything.data.fields.*
import eu.automateeverything.domain.configurable.FieldDefinition

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.type,
            field.name,
            field.hint,
            field.maxSize,
            field.initialValueAsString(),
            if (field.reference != null) {
                if (field.reference!! is PortReference) {
                    ReferenceDto(field.reference!!.clazz.name,
                        (field.reference!! as PortReference).type.toString())
                } else if (field.reference!! is InstanceReference) {
                    ReferenceDto(field.reference!!.clazz.name,
                        (field.reference!! as InstanceReference).type.toString())
                } else {
                    null
                }
            } else {
                null
            },
            field.values
        )
    }
}