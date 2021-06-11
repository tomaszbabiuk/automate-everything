package eu.geekhome.rest.settinggroup

import eu.geekhome.rest.MappingException
import eu.geekhome.rest.field.FieldDefinitionDtoMapper
import eu.geekhome.domain.configurable.*
import java.util.stream.Collectors
import javax.inject.Inject

class SettingGroupDtoMapper @Inject constructor(
    private val fieldDefinitionDtoMapper: FieldDefinitionDtoMapper
) {
    @Throws(MappingException::class)
    fun map(category: SettingGroup): SettingGroupDto {
        val fields: List<FieldDefinitionDto>? = category
            .fieldDefinitions
            .values
            .stream()
            .map { field: FieldDefinition<*> -> fieldDefinitionDtoMapper.map(field) }
            .collect(Collectors.toList())

        return SettingGroupDto(
            category.javaClass.name,
            category.titleRes,
            category.descriptionRes,
            fields,
            category.iconRaw
        )
    }
}