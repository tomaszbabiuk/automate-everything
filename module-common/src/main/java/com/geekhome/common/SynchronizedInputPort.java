package com.geekhome.common;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.hardwaremanager.IInputPort;

public class SynchronizedInputPort<T> extends ConnectiblePortBase implements IInputPort<T> {
    private ILogger _logger = LoggingService.getLogger();
    private T _value;

    public SynchronizedInputPort(String id, long connectionLostInterval) {
        super(id, connectionLostInterval);
    }

    public SynchronizedInputPort(String id, T initialValue, long connectionLostInterval) {
        this(id, connectionLostInterval);
        setValue(initialValue);
    }

    @Override
    public T read() {
        return getValue();
    }

    public synchronized void setValue(T value) {
        if (!value.equals(_value)) {
            _logger.activity(getId(), value, _value);
        }

        _value = value;
    }

    public synchronized T getValue() {
        return _value;
    }
}
