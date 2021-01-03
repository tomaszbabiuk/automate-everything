package eu.geekhome.services.configurable;

import com.geekhome.common.localization.Resource;

public class MaxStringLengthValidator implements Validator<String> {

    private int _maxLength;

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

/*
class MaxStringLengthValidator(private val maxLength: Int)
    : Validator<String>
{
    override var reason: Int = R.string.validator_max_length

    override fun validate(fieldData: String): Boolean {
        if (fieldData.isNotEmpty()) {
            return fieldData.length <= maxLength
        }

        return true
    }
}
 */
