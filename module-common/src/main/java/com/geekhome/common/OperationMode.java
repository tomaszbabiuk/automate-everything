package com.geekhome.common;

public enum OperationMode {
    Automatic(1), // automation on, steering units only
    Configuration(4), // automation off, configuration units only
    Diagnostics(3); // automation off

    private final int _index;

    OperationMode(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static OperationMode fromInt(int value) {
        for(OperationMode val : values()) {
            if (val.toInt() == value) {
                return val;
            }
        }

        throw new ClassCastException("OperationMode enum doesn't contains an element of value " + value);
    }
}
