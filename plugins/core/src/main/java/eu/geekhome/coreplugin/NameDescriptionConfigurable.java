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

    private StringField nameField = new StringField("name", R.field_name_hint,
            new CombinedValidator<>(new RequiredValidator(), new MaxStringLengthValidator(20)));

    private StringField descriptionField = new StringField("description", R.field_description_hint,
            new CombinedValidator<>(new RequiredValidator(), new MaxStringLengthValidator(20)));
}