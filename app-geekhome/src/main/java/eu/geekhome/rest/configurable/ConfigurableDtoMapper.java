package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.MappingException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDefinitionDtoMapper _fieldDefinitionDtoMapper;

    public ConfigurableDto map(Configurable configurable) throws MappingException {
        List<FieldDto> fields = configurable.getFieldDefinitions() == null ? null : configurable
                .getFieldDefinitions()
                .stream()
                .map(_fieldDefinitionDtoMapper::map)
                .collect(Collectors.toList());


        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getDescriptionRes(),
                configurable.getClass().getSimpleName(),
                configurable.getParent() != null ? configurable.getParent().getSimpleName() : null,
                fields,
                configurable.getAddNewRes(),
                configurable.getEditRes(),
                configurable.getIconRaw());
    }
}
