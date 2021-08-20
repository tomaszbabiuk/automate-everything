package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.data.localization.Resource

class RequiredStringValidator : Validator<String?> {

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null) {
            return false
        }

        return validatedFieldValue.isNotEmpty()
    }

    override val reason: Resource = R.validator_required_field
}