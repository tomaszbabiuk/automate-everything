package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public class StringField extends FieldDefinition<String> {

    public StringField(String name, Resource hint, Validator<String> validator) {
        super(name, hint, String.class, new StringFieldBuilder(), validator);
    }
}

/*
class StringField(
    name: String,
    hint: Int,
    validator: Validator<String>
) : Field<String>(name, hint, String::class, StringFieldBuilder(), validator) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        this.value = value
    }
}
 */
