package eu.geekhome.services.configurable;

public interface FieldBuilder<T> {
    T fromPersistableString(String value);
    String toPersistableString(T value);
}

/*
interface FieldBuilder<T> {
    fun fromPersistableString(value : String) : T
    fun toPersistableString(value : T) : String
}
*/
