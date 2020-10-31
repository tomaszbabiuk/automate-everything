package com.geekhome.common;

import java.util.Calendar;

public class ConnectiblePortBase extends PortBase implements IConnectible {
    private boolean _isConnected;
    private long _lastSeenTimestamp;
    private long _connectionLostInterval;

    public ConnectiblePortBase(String id, long connectionLostInterval) {
        super(id);
        _connectionLostInterval = connectionLostInterval;
        _isConnected = true;
    }

    @Override
    public boolean isConnected(Calendar now) {
        if (_connectionLostInterval == 0) {
            return true;
        }

        boolean lastSeenTimeElapsed = now.getTimeInMillis() > _lastSeenTimestamp + _connectionLostInterval;
        return _isConnected && !lastSeenTimeElapsed;
    }

    @Override
    public void markDisconnected() {
        _isConnected = false;
    }

    @Override
    public void setConnectionLostInterval(long intervalInMs) {
        _connectionLostInterval = intervalInMs;
    }

    @Override
    public void updateLastSeenTimestamp(long lastSeenTimestampInMs) {
        _lastSeenTimestamp = lastSeenTimestampInMs;
        _isConnected = true;
    }
}
