package com.geekhome.common.configuration;

import com.geekhome.common.*;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.List;

public class Mode extends NamedObject implements IPrioritized, IBlocksTarget {
    private int _priority;

    @Persistable(name="Priority")
    public int getPriority() {
        return _priority;
    }

    public void setPriority(int value) {
        _priority = value;
    }

    public Mode(DescriptiveName name, int priority) {
        super(name);
        setPriority(priority);
    }

    @Override
    public List<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        return null;
    }

    public boolean equals(Object obj) {
        boolean equalsToNamedObject = obj instanceof INamedObject && ((INamedObject) obj).getName().getUniqueId().equals(getName().getUniqueId());
        boolean equalsToUniqueId = obj instanceof String && obj.equals(getName().getUniqueId());

        return equalsToNamedObject || equalsToUniqueId;
    }
}
