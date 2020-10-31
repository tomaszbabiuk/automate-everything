package com.geekhome.common.localization;

import java.util.Hashtable;

public class Resource {
    private String _key;
    private Hashtable<Language, String> _values;

    public Resource(String key, String englishValue, String polishValue) {
        _key = key;
        _values = new Hashtable<>();
        _values.put(Language.EN, englishValue);
        _values.put(Language.PL, polishValue);
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public String getValue(Language language) {
        return _values.get(language);
    }
}
