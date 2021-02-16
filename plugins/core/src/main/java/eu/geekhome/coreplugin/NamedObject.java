package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;

public class NamedObject implements INamedObject {

    private final Resource _description;
    private final Resource _name;
    private final String _id;

    public NamedObject(String id, Resource description, Resource name) {
        _id = id;
        _description = description;
        _name = name;
    }

    @Override
    public Resource getName() {
        return _name;
    }

    @Override
    public Resource getDescription() {
        return _description;
    }

    @Override
    public String getId() {
        return _id;
    }
}
