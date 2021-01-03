package eu.geekhome.shellyplugin;

import com.geekhome.common.SynchronizedOutputPort;

public abstract class ShellyOutputPort<T> extends SynchronizedOutputPort<T> implements IShellyOutputPort {

    private String _writeTopic;
    private String _readTopic;
    private boolean _valueChanged;

    public ShellyOutputPort(String id, T initialValue, String readTopic, String writeTopic, long connectionLostInterval) {
        super(id, initialValue, connectionLostInterval);
        _readTopic = readTopic;
        _writeTopic = writeTopic;
    }

    public String getReadTopic() {
        return _readTopic;
    }

    public String getWriteTopic() {
        return _writeTopic;
    }

    @Override
    public void write(T value) throws Exception {
        T currentValue = read();
        if (value != currentValue) {
            super.write(value);
            _valueChanged = true;
        }
    }

    @Override
    public void resetLatch() {
        _valueChanged = false;
    }

    @Override
    public boolean didChangeValue() {
        return _valueChanged;
    }
}
