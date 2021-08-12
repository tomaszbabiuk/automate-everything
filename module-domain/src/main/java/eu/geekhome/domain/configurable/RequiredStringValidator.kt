package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.data.localization.Resource

class RequiredStringValidator : Validator<String?> {

    override fun validate(fieldValue: String?): Boolean {
        if (fieldValue == null) {
            return false
        }

        return fieldValue.isNotEmpty()
    }

    override val reason: Resource = R.validator_required_field
}