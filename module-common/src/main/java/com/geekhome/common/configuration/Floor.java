package com.geekhome.common.configuration;

import com.geekhome.common.ConfigurationSaver;
import com.geekhome.common.NamedObject;

public class Floor extends NamedObject {
    private CollectorCollection<Room> _rooms;
    private String _iconName;

    @Persistable(name="Rooms")
    @ConfigurationSaver(sectionName = "Room/Floor", hasChildren = false)
    public CollectorCollection<Room> getRooms() {
        return _rooms;
    }

    private void setRooms(CollectorCollection<Room> value) {
        _rooms = value;
    }

    @Persistable(name="IconName")
    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String value) {
        _iconName = value;
    }

    public Floor(DescriptiveName name, String iconName) {
        super(name);
        setIconName(iconName);
        setRooms(new CollectorCollection<Room>());
    }
}
