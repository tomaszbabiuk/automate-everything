package eu.automateeverything.domain.configurable

import eu.automateeverything.domain.R
import eu.automateeverything.data.localization.Resource

class RequiredStringValidator : Validator<String?> {

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null) {
            return false
        }

        return validatedFieldValue.isNotEmpty()
    }

    override val reason: Resource = R.validator_required_field
}