package eu.automateeverything.rest.configurables

import eu.automateeverything.data.configurables.ConfigurableDto
import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.rest.MappingException
import eu.automateeverything.rest.fields.FieldDefinitionDtoMapper
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.data.localization.Resource
import java.util.stream.Collectors
import jakarta.inject.Inject

class ConfigurableDtoMapper @Inject constructor(
    private val fieldDefinitionDtoMapper: FieldDefinitionDtoMapper
) {
    @Throws(MappingException::class)
    fun map(configurable: Configurable): ConfigurableDto {
        var fields: List<FieldDefinitionDto>? = null
        var addNewRes: Resource? = null
        var editRes: Resource? = null
        if (configurable is ConfigurableWithFields) {
            fields = configurable
                .fieldDefinitions
                .values
                .stream()
                .map { field: FieldDefinition<*> -> fieldDefinitionDtoMapper.map(field) }
                .collect(Collectors.toList())
            addNewRes = configurable.addNewRes
            editRes = configurable.editRes
        }

        return ConfigurableDto(
            configurable.titleRes,
            configurable.descriptionRes,
            configurable.javaClass.name,
            if (configurable.parent != null) configurable.parent!!.name else null,
            getValueClazz(configurable)?.name,
            fields,
            addNewRes,
            editRes,
            configurable.iconRaw,
            configurable.hasAutomation,
            configurable.editableIcon,
            configurable.taggable
        )
    }

    private fun getValueClazz(configurable: Configurable): Class<out Any?>? {
        if (configurable is DeviceConfigurable<*>) {
            return configurable.valueClazz
        }

        return null
    }
}