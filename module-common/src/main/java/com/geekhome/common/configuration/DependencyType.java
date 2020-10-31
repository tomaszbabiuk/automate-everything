package com.geekhome.common.configuration;

public enum DependencyType {
    Room(0),
    Device(1),
    Condition(3),
    Block(4),
    Mode(5),
    Other(6),
    Zone(7),
    Command(8);

    private int _index;

    private DependencyType(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }
}
