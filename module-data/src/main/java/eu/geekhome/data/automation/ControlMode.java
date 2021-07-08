package eu.geekhome.data.automation;

public enum ControlMode {
    Manual(0),
    Auto(1),
    ForcedManual(2),
    AutomationBreak(3);

    private int _index;

    ControlMode(int index) {
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
