package com.geekhome.common;

public enum DeviceCategory {
    Floor(0),
    Heating(1),
    HotWater(2),
    Locks(3),
    Lights(4),
    Map(5),
    Ventilation(6),
    Monitoring(7);

    private final int _index;

    DeviceCategory(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static DeviceCategory fromInt(int i) {
        for(DeviceCategory value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
