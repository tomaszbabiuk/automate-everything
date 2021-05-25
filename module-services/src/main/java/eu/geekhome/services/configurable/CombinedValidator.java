package eu.geekhome.services.configurable;


import eu.geekhome.services.localization.Resource;

public class CombinedValidator<T> implements Validator<T> {

    private final Validator<T>[] _validators;

    public CombinedValidator(Validator<T> ... validators) {
        _validators = validators;
    }

    @Override
    public Resource getReason() {
        return null;
    }

    @Override
    public boolean validate(T fieldValue) {
        for (Validator<T> validator : _validators) {
            if (!validator.validate(fieldValue)) {
                return false;
            }
        }

        return true;
    }
}
