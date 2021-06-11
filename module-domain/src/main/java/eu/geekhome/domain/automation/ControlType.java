package eu.geekhome.domain.automation;

public enum ControlType {
    ReadValue(0),
    Multicontroller(2),
    Code(5),
    Multistate(7);

    private int _index;

    ControlType(int index) {
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
