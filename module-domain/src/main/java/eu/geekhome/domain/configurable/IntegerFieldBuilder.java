package eu.geekhome.domain.configurable;

public class IntegerFieldBuilder implements FieldBuilder<Integer> {
    @Override
    public Integer fromPersistableString(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String toPersistableString(Integer value) {
        return value.toString();
    }
}