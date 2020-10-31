package com.geekhome.common;

import java.util.Calendar;

public interface IConnectible {
    boolean isConnected(Calendar now);
    void markDisconnected();
    void setConnectionLostInterval(long intervalInMs);
    void updateLastSeenTimestamp(long lastSeenTimestampInMs);
}
