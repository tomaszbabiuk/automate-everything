package eu.geekhome.domain.configurable;

public class StringFieldBuilder implements FieldBuilder<String> {
    @Override
    public String fromPersistableString(String value) {
        return value;
    }

    @Override
    public String toPersistableString(String value) {
        return value;
    }
}
