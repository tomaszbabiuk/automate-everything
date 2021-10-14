package eu.automateeverything.onewireplugin;

public enum SwitchRole {
    AlarmSensing(0),
    RelayBoard(1),
    BothConfliting(2),
    Unknown(3);

    private int _index;

    SwitchRole(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static SwitchRole fromInt(int i) {
        for(SwitchRole value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}