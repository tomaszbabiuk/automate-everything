package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public abstract class FieldDefinition<T> {
    private String _name;
    private Resource _hint;
    private Class<T> _valueClazz;
    private FieldBuilder<T> _builder;
    private Validator<T> _validator;

    public String getName() {
        return _name;
    }

    public Resource getHint() {
        return _hint;
    }

    public Class<T> getValueClazz() {
        return _valueClazz;
    }

    public Validator<T> getValidator() {
        return _validator;
    }

    public FieldBuilder<T> getBuilder() {
        return _builder;
    }

    protected FieldDefinition(String name, Resource hint, Class<T> valueClazz, FieldBuilder<T> builder, Validator<T> validator) {
        _name = name;
        _hint = hint;
        _valueClazz = valueClazz;
        _builder = builder;
        _validator = validator;
    }

    public boolean isRequired() {
        return false;//TODO
    }

    public int getMaxSize() {
        return 30; //TODO
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
