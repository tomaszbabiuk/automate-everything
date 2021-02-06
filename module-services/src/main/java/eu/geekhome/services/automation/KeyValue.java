package eu.geekhome.services.automation;

public class KeyValue
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
}