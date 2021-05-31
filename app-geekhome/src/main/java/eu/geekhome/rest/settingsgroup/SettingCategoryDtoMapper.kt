package eu.geekhome.rest.settingsgroup

import eu.geekhome.rest.MappingException
import eu.geekhome.rest.field.FieldDefinitionDtoMapper
import eu.geekhome.services.configurable.*
import java.util.stream.Collectors
import javax.inject.Inject

class SettingCategoryDtoMapper @Inject constructor(
    private val fieldDefinitionDtoMapper: FieldDefinitionDtoMapper
) {
    @Throws(MappingException::class)
    fun map(category: SettingsCategory): SettingCategoryDto {
        val fields: List<FieldDefinitionDto>? = category
            .fieldDefinitions
            .values
            .stream()
            .map { field: FieldDefinition<*> -> fieldDefinitionDtoMapper.map(field) }
            .collect(Collectors.toList())

        return SettingCategoryDto(
            category.javaClass.name,
            category.titleRes,
            category.descriptionRes,
            fields,
            category.iconRaw
        )
    }
}