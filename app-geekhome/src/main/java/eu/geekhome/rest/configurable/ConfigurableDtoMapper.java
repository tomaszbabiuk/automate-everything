package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.*;
import eu.geekhome.rest.MappingException;
import eu.geekhome.services.localization.Resource;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDefinitionDtoMapper _fieldDefinitionDtoMapper;

    public ConfigurableDto map(Configurable configurable) throws MappingException {
        List<FieldDto> fields = null;
        Resource addNewRes = null;
        Resource editRes = null;
        if (configurable instanceof ConfigurableWithFields) {
            ConfigurableWithFields configurableWithFields = (ConfigurableWithFields)configurable;
            fields = configurableWithFields
                    .getFieldDefinitions()
                    .values()
                    .stream()
                    .map(_fieldDefinitionDtoMapper::map)
                    .collect(Collectors.toList());

            addNewRes = configurableWithFields.getAddNewRes();
            editRes = configurableWithFields.getEditRes();
        }

        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getDescriptionRes(),
                configurable.getClass().getName(),
                configurable.getParent() != null ? configurable.getParent().getName() : null,
                getConfigurableType(configurable),
                fields,
                addNewRes,
                editRes,
                configurable.getIconRaw(),
                configurable.getHasAutomation(),
                configurable.getEditableIcon(),
                configurable.getTaggable());
    }

    private ConfigurableType getConfigurableType(Configurable configurable) {
        if (configurable instanceof ConditionConfigurable) {
            return ConfigurableType.Condition;
        }

        if (configurable instanceof StateDeviceConfigurable) {
            return ConfigurableType.StateDevice;
        }

        return ConfigurableType.Other;
    }
}
