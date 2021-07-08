package eu.geekhome.data.automation;

public enum StateType {
    ReadOnly(0),
    Control(1),
    SignaledAction(2),
    NonSignaledAction(3);

    private final int _index;

    StateType(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }
}