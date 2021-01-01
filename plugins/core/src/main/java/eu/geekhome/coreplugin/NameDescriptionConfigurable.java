package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.*;

import java.util.ArrayList;
import java.util.List;

public abstract class NameDescriptionConfigurable implements Configurable {

    @Override
    public List<FieldDefinition<?>> getFieldDefinitions() {
        ArrayList<FieldDefinition<?>> result = new ArrayList<>();
        result.add(nameField);
        result.add(descriptionField);
        return result;
    }

    private final StringField nameField = new StringField("name", R.field_name_hint, 50,
            new RequiredStringValidator(), new MaxStringLengthValidator(50));

    private final StringField descriptionField = new StringField("description", R.field_description_hint, 200,
            new MaxStringLengthValidator(200));
}