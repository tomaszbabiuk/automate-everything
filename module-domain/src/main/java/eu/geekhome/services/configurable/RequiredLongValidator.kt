package eu.geekhome.services.configurable

import eu.geekhome.services.R
import eu.geekhome.services.localization.Resource

class RequiredLongValidator : Validator<Long> {

    override fun validate(fieldValue: Long?): Boolean {
        if (fieldValue == null) {
            return false
        }

        return fieldValue > 0
    }

    override val reason: Resource = R.validator_required_field
}