package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public class RequiredLongValidator implements Validator<Long> {
    @Override
    public Resource getReason() {
        return new Resource("This field is required",
                "To pole jest wymagane");
    }

    @Override
    public boolean validate(Long fieldValue) {
        return fieldValue != null && fieldValue > 0;
    }
}