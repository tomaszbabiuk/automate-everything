package eu.geekhome.rest.configurables

import eu.geekhome.data.configurables.ConfigurableDto
import eu.geekhome.data.configurables.ConfigurableType
import eu.geekhome.data.fields.FieldDefinitionDto
import eu.geekhome.rest.MappingException
import eu.geekhome.rest.fields.FieldDefinitionDtoMapper
import eu.geekhome.domain.configurable.*
import eu.geekhome.data.localization.Resource
import java.util.stream.Collectors
import javax.inject.Inject

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
            getConfigurableType(configurable),
            fields,
            addNewRes,
            editRes,
            configurable.iconRaw,
            configurable.hasAutomation,
            configurable.editableIcon,
            configurable.taggable
        )
    }

    private fun getConfigurableType(configurable: Configurable): ConfigurableType {
        if (configurable is ConditionConfigurable) {
            return ConfigurableType.Condition
        }
        return if (configurable is StateDeviceConfigurable) {
            ConfigurableType.StateDevice
        } else ConfigurableType.Other
    }
}