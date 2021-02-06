package eu.geekhome.coreplugin;

import eu.geekhome.services.configurable.BlockTarget;
import eu.geekhome.services.configurable.ConfigurableType;
import eu.geekhome.services.localization.Resource;
import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.configurable.FieldDefinition;

import java.util.List;

public abstract class CategoryConfigurable implements Configurable {

    @Override
    public Resource getAddNewRes() {
        return null;
    }

    @Override
    public Resource getEditRes() { return null; }

    @Override
    public List<FieldDefinition<?>> getFieldDefinitions() {
        return null;
    }

    @Override
    public List<BlockTarget> getBlockTargets() {
        return null;
    }

    @Override
    public ConfigurableType getType() {
        return ConfigurableType.Category;
    }
}
