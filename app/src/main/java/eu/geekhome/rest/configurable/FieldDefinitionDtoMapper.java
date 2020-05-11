package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.FieldDefinition;

public class FieldDefinitionDtoMapper {

    public FieldDto map(FieldDefinition<?> field) {
        return new FieldDto(field.getClass().getSimpleName() , field.getName(), field.getHint(),
                field.isRequired(), field.getMaxSize());
    }
}
