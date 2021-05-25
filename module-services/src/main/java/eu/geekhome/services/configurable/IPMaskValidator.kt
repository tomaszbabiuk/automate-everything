package eu.geekhome.services.configurable

import eu.geekhome.services.R
import eu.geekhome.services.localization.Resource

class IPMaskValidator : Validator<String> {
    override val reason: Resource = R.validator_required_field

    override fun validate(fieldValue: String): Boolean {
        //TODO: implement ip mask checking logic
        return fieldValue.isNotEmpty()
    }
}