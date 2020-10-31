package com.geekhome.common.automation;

import java.util.Calendar;

public class MinWorkingTimeCounter {
    private boolean _lastState;
    private long _maxMillis;
    private long _switchedOnMillis;
    private long _lastOnMillis;

    public MinWorkingTimeCounter(boolean initialState, long maxMillis) {
        _lastState = initialState;
        _maxMillis = maxMillis;
        reset();
    }

    public boolean isExceeded() {
        return _switchedOnMillis > _maxMillis;
    }

    public void signal(boolean newState) {
        if (newState && _lastState) {
            long now = Calendar.getInstance().getTimeInMillis();
            long delta = now - _lastOnMillis;
            _switchedOnMillis += delta;
            _lastOnMillis = now;
        } else {
            reset();
        }

        _lastState = newState;
    }

    private void reset() {
        _switchedOnMillis = 0;
        _lastOnMillis = Calendar.getInstance().getTimeInMillis();
    }
}
