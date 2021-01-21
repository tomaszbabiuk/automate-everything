package com.geekhome.common;

public class SectionMetadata {
    private String _name;
    private final String _parentUniqueId;
    private final INameValueSet _properties;

    public SectionMetadata(String name, String parentUniqueId, INameValueSet properties) {
        _name = name;
        _parentUniqueId = parentUniqueId;
        _properties = properties;
    }

    public String getUniqueId() {
        if (_properties.getKeys().contains("uniqueid")) {
            return _properties.getValue("uniqueid");
        }

        return null;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getParentUniqueId() {
        return _parentUniqueId;
    }

    public INameValueSet getProperties() {
        return _properties;
    }
}
