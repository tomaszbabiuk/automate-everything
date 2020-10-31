package com.geekhome.common;

public class DictionaryEntry {
    private Object _key;
    private Object _value;

    public Object getKey() {
        return _key;
    }

    public void setKey(Object key) {
        _key = key;
    }

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    public DictionaryEntry(String key, String value) {
        setKey(key);
        setValue(value);
    }
}
