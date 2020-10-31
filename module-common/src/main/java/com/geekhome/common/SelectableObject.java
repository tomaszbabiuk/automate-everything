package com.geekhome.common;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;

public class SelectableObject extends NamedObject {
    private boolean _selected;

    @Persistable(name="Selected")
    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean value) {
        _selected = value;
    }

    public SelectableObject(DescriptiveName name, boolean selected) {
        super(name);
        _selected = selected;
    }
}
