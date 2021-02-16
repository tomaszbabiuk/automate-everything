package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;

public class ResourceWithIdDto {
    private final String _id;
    private final Resource _label;

    public Resource getLabel() {
        return _label;
    }
    public String getId() {
        return _id;
    }

    public ResourceWithIdDto(String id, Resource label) {
        _id = id;
        _label = label;
    }
}