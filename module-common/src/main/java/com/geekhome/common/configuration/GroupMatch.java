package com.geekhome.common.configuration;

public enum GroupMatch
{
    AllInTheGroup(0),
    OneOfTheGroup(1);

    private final int _index;

    GroupMatch(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static GroupMatch fromInt(int i) {
        for(GroupMatch value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
