package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.*;
import eu.geekhome.rest.MappingException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDefinitionDtoMapper _fieldDefinitionDtoMapper;

    @Inject
    private BlockTargetDtoMapper _blockTargetDtoMapper;

    public ConfigurableDto map(Configurable configurable) throws MappingException {
        List<FieldDto> fields = configurable.getFieldDefinitions() == null ? null : configurable
                .getFieldDefinitions()
                .values()
                .stream()
                .map(_fieldDefinitionDtoMapper::map)
                .collect(Collectors.toList());

        List<ResourceWithIdDto> blockTargets = null;
        if (configurable instanceof StateDeviceConfigurable) {
            blockTargets = ((StateDeviceConfigurable)configurable)
                    .getBlockTargets()
                    .stream()
                    .map(_blockTargetDtoMapper::map)
                    .collect(Collectors.toList());
        }

        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getDescriptionRes(),
                configurable.getClass().getName(),
                configurable.getParent() != null ? configurable.getParent().getName() : null,
                fields,
                blockTargets,
                configurable.getAddNewRes(),
                configurable.getEditRes(),
                configurable.getIconRaw());
    }
}
