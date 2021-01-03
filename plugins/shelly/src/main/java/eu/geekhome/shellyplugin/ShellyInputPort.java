package eu.geekhome.shellyplugin;

import com.geekhome.common.SynchronizedInputPort;

public abstract class ShellyInputPort<T> extends SynchronizedInputPort<T> implements IShellyPort {
    private String _readTopic;

    public ShellyInputPort(String id, T initialValue, String readTopic, long connectionLostInterval) {
        super(id, initialValue, connectionLostInterval);
        _readTopic = readTopic;
    }

    public String getReadTopic() {
        return _readTopic;
    }
}
