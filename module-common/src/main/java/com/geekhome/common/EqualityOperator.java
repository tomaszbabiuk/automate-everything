package com.geekhome.common;

public enum EqualityOperator
{
    GreaterOrEqual(0),
    Lower(1);

    private final int _index;

    private EqualityOperator(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static EqualityOperator fromInt(int i) {
        for(EqualityOperator value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
