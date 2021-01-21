package com.geekhome.common.configuration;

public enum InactiveState {
    NC(0),
    NO(1);

    private final int _index;

    InactiveState(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static InactiveState fromInt(int i) {
        for(InactiveState value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}