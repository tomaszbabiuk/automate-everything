package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.FieldDefinition;
import eu.geekhome.services.configurable.FieldDto;

public class FieldDefinitionDtoMapper {

    public FieldDto map(FieldDefinition<?> field) {
        return new FieldDto(field.getClass().getSimpleName() , field.getName(), field.getHint(),
                field.getMaxSize());
    }
}
