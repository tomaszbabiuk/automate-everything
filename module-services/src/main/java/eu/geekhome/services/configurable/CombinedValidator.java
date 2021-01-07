package eu.geekhome.services.configurable;

import com.geekhome.common.localization.Resource;

public class CombinedValidator<T> implements Validator<T> {

    private Validator<T>[] _validators;

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

/*
class CombinedValidator<T>(val validators: Array<Validator<T>>)
    : Validator<T>
{
    override var reason: Int = R.string.validator_not_set

    override fun validate(fieldData: T): Boolean {
        validators.forEach {
            if (!it.validate(fieldData)) {
                reason = it.reason
                return false
            }
        }

        return true
    }
}

 */
