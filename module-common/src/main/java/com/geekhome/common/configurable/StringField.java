package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public class StringField extends FieldDefinition<String> {

    public StringField(String name, Resource hint, int maxSize, Validator<String>... validators) {
        super(name, hint, maxSize, String.class, new StringFieldBuilder(), validators);
    }
}
