package eu.geekhome.rest.configurable;

import com.geekhome.common.configurable.Field;
import eu.geekhome.rest.configurable.FieldDto;

public class FieldDtoMapper {

    public FieldDto map(Field<?> field) {
        String valueAsString = field.getValue() == null ? null : field.getValue().toString();
        return new FieldDto(field.getClass().getSimpleName() , field.getName(), field.getHint(), valueAsString);
    }
}
