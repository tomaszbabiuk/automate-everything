package com.geekhome.common;

import java.util.ArrayList;

public class NameValueSet implements INameValueSet {
        private final ArrayList<DictionaryEntry> _wrapper;
        private final ArrayList<String> _keys;

        public NameValueSet()
        {
            _wrapper = new ArrayList<>();
            _keys = new ArrayList<>();
        }

    public String getValue(String parameter)
    {
        for (DictionaryEntry entry : _wrapper)
        {
            if (entry.getKey().toString().equalsIgnoreCase(parameter))
            {
                return (String)entry.getValue();
            }
        }
        return null;
    }

    public String getValues(String parameter)
    {
        String result = "";
        for (DictionaryEntry entry : _wrapper)
        {
            if (((String) entry.getKey()).toLowerCase().equals(parameter))
            {
                if (!result.equals(""))
                {
                    result += ",";
                }
                result += (String)entry.getValue();
            }
        }

        return result;
    }

    public void add(String key, String value)
    {
        DictionaryEntry entry = new DictionaryEntry(key, value);
        if (!_keys.contains(key))
        {
            _keys.add(key);
        }
        _wrapper.add(entry);
    }

    public ArrayList<String> getKeys() {
        return _keys;
    }
}
