package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.*;
import com.geekhome.common.localization.Resource;

import java.util.List;

public abstract class CategoryConfigurable implements Configurable {

    @Override
    public Resource getAddNewRes() {
        return null;
    }

    @Override
    public List<FieldDefinition<?>> getFieldDefinitions() {
        return null;
    }
}
