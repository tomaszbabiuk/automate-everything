package com.geekhome.common.configuration;

public enum YesNo {
    No(0),
    Yes(1);

    private final int _index;

    YesNo(int index) {

        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static YesNo fromInt(int i) {
        for(YesNo value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
