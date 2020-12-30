package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldDefinition<T> {
    private String _name;
    private Resource _hint;
    private int _maxSize;
    private Class<T> _valueClazz;
    private FieldBuilder<T> _builder;
    private Validator<T>[] _validators;

    public String getName() {
        return _name;
    }

    public Resource getHint() {
        return _hint;
    }

    public Class<T> getValueClazz() {
        return _valueClazz;
    }

    public Validator<T>[] getValidators() {
        return _validators;
    }

    public FieldBuilder<T> getBuilder() {
        return _builder;
    }

    public int getMaxSize() {
        return _maxSize;
    }

    protected FieldDefinition(String name, Resource hint, int maxSize, Class<T> valueClazz, FieldBuilder<T> builder, Validator<T>... validators) {
        _name = name;
        _hint = hint;
        _maxSize = maxSize;
        _valueClazz = valueClazz;
        _builder = builder;
        _validators = validators;
    }

    public FieldValidationResult validate(String valueAsString) {
        boolean isFieldValid = true;
        List<Resource> failingReasons = new ArrayList<>();
        T value = _builder.fromPersistableString(valueAsString);

        for (Validator<T> validator : _validators) {
            boolean isValid = validator.validate(value);
            if (!isValid) {
                isFieldValid = false;
                failingReasons.add(validator.getReason());
            }
        }

        return new FieldValidationResult(isFieldValid, failingReasons);
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
