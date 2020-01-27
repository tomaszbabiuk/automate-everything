package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Configurable;
import eu.geekhome.rest.MappingException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableDtoMapper {

    @Inject
    private FieldDefinitionDtoMapper _fieldDefinitionDtoMapper;

    public ConfigurableDto map(Configurable configurable, List<Configurable> allConfigurables) throws MappingException {
        List<FieldDto> fields = configurable
                .getFieldDefs()
                .stream()
                .map(_fieldDefinitionDtoMapper::map)
                .collect(Collectors.toList());

//        List<String> attachableTo = configurable.attachableTo() == null ? null : configurable
//                .attachableTo()
//                .stream()
//                .map(this::mapClassToString)
//                .collect(Collectors.toList());

        List<ConfigurableDto> children = new ArrayList<>();

        return new ConfigurableDto(configurable.getTitleRes(),
                configurable.getClass().getSimpleName(),
                fields,
                configurable.getAddNewRes(),
                configurable.getIconName(),
                children);
    }

    private String mapClassToString(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
