package eu.geekhome.domain.localization;

import java.util.Hashtable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(_values, resource._values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_values);
    }
}

