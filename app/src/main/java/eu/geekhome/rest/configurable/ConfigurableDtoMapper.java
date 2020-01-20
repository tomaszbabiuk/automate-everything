package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.MappingException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDtoMapper _fieldDtoMapper;

    public ConfigurableDto map(Configurable configurable) throws MappingException {
        List<FieldDto> fields = configurable
                .getFields()
                .stream()
                .map(_fieldDtoMapper::map)
                .collect(Collectors.toList());

        List<String> attachableTo = configurable.attachableTo() == null ? null : configurable
                .attachableTo()
                .stream()
                .map(this::mapClassToString)
                .collect(Collectors.toList());

        return new ConfigurableDto(configurable.getClass().getSimpleName(), fields, attachableTo,
                configurable.getPersistenceId(), configurable.getAddNewRes(), configurable.getIconName());
    }

    private String mapClassToString(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
