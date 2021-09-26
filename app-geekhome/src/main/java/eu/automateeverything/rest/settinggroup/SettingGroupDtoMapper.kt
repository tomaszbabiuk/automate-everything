package eu.automateeverything.rest.settinggroup

import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.data.settings.SettingGroupDto
import eu.automateeverything.rest.MappingException
import eu.automateeverything.rest.fields.FieldDefinitionDtoMapper
import eu.automateeverything.domain.configurable.*
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
            fields
        )
    }
}