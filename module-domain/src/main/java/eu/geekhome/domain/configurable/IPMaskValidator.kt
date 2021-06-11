package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.domain.localization.Resource

class IPMaskValidator : Validator<String> {
    override val reason: Resource = R.validator_required_field

    override fun validate(fieldValue: String?): Boolean {
        if (fieldValue == null) {
            return false
        }
        //TODO: implement ip mask checking logic
        return fieldValue.isNotEmpty()
    }
}