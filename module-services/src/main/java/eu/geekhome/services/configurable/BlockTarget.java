package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;

public class BlockTarget {
    private String _id;
    private Resource _label;

    public BlockTarget(String id, Resource label) {
        _id = id;
        _label = label;
    }

    public Resource getLabel() {
        return _label;
    }

    public String getId() {
        return _id;
    }
}
