package eu.geekhome.services.configurable

import eu.geekhome.services.R
import eu.geekhome.services.localization.Resource

class RequiredStringValidator : Validator<String> {

    override fun validate(fieldValue: String): Boolean {
        return fieldValue.isNotEmpty()
    }

    override val reason: Resource = R.validator_required_field
}