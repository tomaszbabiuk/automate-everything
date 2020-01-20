package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.*;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
public class HouseConfigurable implements Configurable {

    @Override
    public List<Field<?>> getFields() {
        ArrayList<Field<?>> result = new ArrayList<>();
        result.add(nameField);
        return result;
    }

    @Override
    public List<Class<Configurable>> attachableTo() {
        return null;
    }

    @Override
    public Long getPersistenceId() {
        return 0L;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_house_add;
    }

    @Override
    public String getIconName() {
        return "house";
    }

    private StringField nameField = new StringField("name", R.field_name_hint,
            new CombinedValidator<>(new RequiredValidator(), new MaxStringLengthValidator(20)));
}
