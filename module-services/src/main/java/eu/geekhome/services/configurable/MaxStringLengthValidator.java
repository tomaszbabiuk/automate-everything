package eu.geekhome.services.configurable;


import eu.geekhome.services.localization.Resource;

public class MaxStringLengthValidator implements Validator<String> {

    private final int _maxLength;

    public MaxStringLengthValidator(int maxLength) {
        _maxLength = maxLength;
    }

    @Override
    public Resource getReason() {
        return new Resource("Max length is " + _maxLength + " characters",
                "Maksymalna długość to " + _maxLength + " znaków");
    }

    @Override
    public boolean validate(String fieldValue) {
        if (fieldValue == null) {
            return true;
        }

        return fieldValue.length() <= _maxLength;
    }
}
