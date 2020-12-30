package com.geekhome.common.configurable;

public class LongFieldBuilder implements FieldBuilder<Long> {

    @Override
    public Long fromPersistableString(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public String toPersistableString(Long value) {
        return value.toString();
    }
}
