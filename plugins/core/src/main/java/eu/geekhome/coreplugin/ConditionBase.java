package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;

public class ConditionBase extends NamedObject implements ICondition {

    public ConditionBase(String id, Resource name, Resource description) {
        super(id, name, description);
    }

    @Override
    public String[] getDevicesIds() {
        return new String[0];
    }
}
