package com.geekhome.common;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class KeyValue implements JSONAware
{
    private String _key;
    private String _value;

    public KeyValue(String key, String value)
    {
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    @Override
    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.put("Key", getKey());
        json.put("Value", getValue());
        return json.toJSONString();
    }
}