package eu.geekhome.services.configurable;

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

/*
class StringFieldBuilder : FieldBuilder<String> {

    override fun fromPersistableString(value: String): String {
        return value
    }

    override fun toPersistableString(value: String): String {
        return value
    }
}
 */
