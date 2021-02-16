package eu.geekhome.coreplugin;

import eu.geekhome.services.configurable.*;

import java.util.HashMap;
import java.util.Map;

public abstract class NameDescriptionConfigurable implements Configurable {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";

    @Override
    public Map<String, FieldDefinition<?>> getFieldDefinitions() {
        Map<String,FieldDefinition<?>> result = new HashMap<>();
        result.put(FIELD_NAME, nameField);
        result.put(FIELD_DESCRIPTION, descriptionField);
        return result;
    }

    private final StringField nameField = new StringField(FIELD_NAME, R.field_name_hint, 50,
            new RequiredStringValidator(), new MaxStringLengthValidator(50));

    private final StringField descriptionField = new StringField(FIELD_DESCRIPTION, R.field_description_hint, 200,
            new MaxStringLengthValidator(200));
}