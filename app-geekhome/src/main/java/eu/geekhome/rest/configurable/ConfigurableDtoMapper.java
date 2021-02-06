package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.BlockTargetDto;
import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.rest.MappingException;
import eu.geekhome.services.configurable.ConfigurableDto;
import eu.geekhome.services.configurable.FieldDto;

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
                .stream()
                .map(_fieldDefinitionDtoMapper::map)
                .collect(Collectors.toList());

        List<BlockTargetDto> blockTargets = configurable.getBlockTargets() == null ? null : configurable
                .getBlockTargets()
                .stream()
                .map(_blockTargetDtoMapper::map)
                .collect(Collectors.toList());

        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getDescriptionRes(),
                configurable.getClass().getSimpleName(),
                configurable.getParent() != null ? configurable.getParent().getSimpleName() : null,
                fields,
                blockTargets,
                configurable.getAddNewRes(),
                configurable.getEditRes(),
                configurable.getIconRaw());
    }
}
