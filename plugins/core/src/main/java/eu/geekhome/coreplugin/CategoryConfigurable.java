package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;
import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.configurable.FieldDefinition;
import eu.geekhome.services.localization.ResourceWithId;

import java.util.List;
import java.util.Map;

public abstract class CategoryConfigurable implements Configurable {

    @Override
    public Resource getAddNewRes() {
        return null;
    }

    @Override
    public Resource getEditRes() { return null; }

    @Override
    public Map<String, FieldDefinition<?>> getFieldDefinitions() {
        return null;
    }
}
