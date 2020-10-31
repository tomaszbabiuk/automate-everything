package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class Room extends NamedObject {
    private String _iconName;

    @Persistable(name="IconName")
    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String value) {
        _iconName = value;
    }

    public Room(DescriptiveName name, String iconName) {
        super(name);
        setIconName(iconName);
    }
}
