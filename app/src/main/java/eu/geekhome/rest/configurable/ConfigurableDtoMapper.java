package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.MappingException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDefinitionDtoMapper _fieldDefinitionDtoMapper;

    public ConfigurableDto map(Configurable configurable, List<Configurable> allConfigurables) throws MappingException {
        List<FieldDto> fields = configurable.getFieldDefinitions() == null ? null : configurable
                .getFieldDefinitions()
                .stream()
                .map(_fieldDefinitionDtoMapper::map)
                .collect(Collectors.toList());


        List<ConfigurableDto> descendants = allConfigurables
                .stream()
                .filter((x) -> x.getParent() != null && x.getParent().equals(configurable.getClass()))
                .map((x) -> this.map(x, allConfigurables))
                .collect(Collectors.toList());

        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getClass().getSimpleName(),
                fields,
                configurable.getAddNewRes(),
                configurable.getIconName(),
                descendants);
    }
}
