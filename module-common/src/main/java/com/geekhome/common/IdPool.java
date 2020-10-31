package com.geekhome.common;

public class IdPool {
    private int _currentId;

    public int getCurrentId() {
        return _currentId;
    }

    public void setCurrentId(int value) {
        _currentId = value;
    }

    public IdPool() {
        setCurrentId(0);
    }

    public String next(String prefix) {
        String result = prefix + getCurrentId();
        _currentId++;
        return result;
    }

    public void reset() {
        setCurrentId(0);
    }
}
