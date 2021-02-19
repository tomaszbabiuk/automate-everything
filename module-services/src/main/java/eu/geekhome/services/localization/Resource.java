package eu.geekhome.services.localization;

import java.util.Hashtable;

public class Resource {
    private final Hashtable<Language, String> _values;

    public Resource(String englishValue, String polishValue) {
        _values = new Hashtable<>();
        _values.put(Language.EN, englishValue);
        _values.put(Language.PL, polishValue);
    }

    public String getValue(Language language) {
        return _values.get(language);
    }

    public static Resource createUniResource(String uniValue) {
        return new Resource(uniValue, uniValue);
    }
}

