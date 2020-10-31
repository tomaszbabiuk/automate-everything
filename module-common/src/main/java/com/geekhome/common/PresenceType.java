package com.geekhome.common;

public enum PresenceType
{
    Presence(0),
    Absence(1);

    private int _index;

    private PresenceType(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static PresenceType fromInt(int i) {
        for(PresenceType value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}