package eu.geekhome.domain.configurable;

public abstract class FieldValue<T> {

    private final FieldDefinition<T> _definition;
    private T _value;

    protected FieldValue(FieldDefinition<T> definition) {
        _definition = definition;
    }

    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }

    public String toPersistableString() {
        if (_value == null) {
            return null;
        }

        return _definition.getBuilder().toPersistableString(getValue());
    }

    public void setValueFromString(String valueAsString) {
        _value = (valueAsString == null) ? null : _definition.getBuilder().fromPersistableString(valueAsString);
    }
}

/*
abstract class Field<T : Any>(
    val name: String,
    @StringRes val hint: Int,
    val clazz: KClass<T>,
    private val builder: FieldBuilder<T>,
    val validator: Validator<T>? = null
) {
    var value : T? = null

    fun toPersistableString() : String? {
        if (value == null) {
            return null
        }

        return builder.toPersistableString(value!!)
    }

    fun setValueFromString(valueAsString: String?) {
        value = if (valueAsString == null) {
            null
        } else {
            builder.fromPersistableString(valueAsString)
        }
    }
}
 */
