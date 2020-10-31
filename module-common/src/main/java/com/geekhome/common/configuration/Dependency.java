package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class Dependency extends NamedObject {
    private DependencyType _type;
    private int _level;

    @Persistable(name="Type")
    public DependencyType getType() {
        return _type;
    }

    public void setType(DependencyType value) {
        _type = value;
    }

    @Persistable(name="Level")
    public int getLevel() {
        return _level;
    }

    public void setLevel(int value) {
        _level = value;
    }

    public Dependency(DependencyType type, DescriptiveName name, int level)
    {
        super(name);
        setType(type);
        setLevel(level);
    }
}