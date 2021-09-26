package eu.automateeverything.domain.configurable

import eu.automateeverything.domain.R
import eu.automateeverything.data.localization.Resource

class RequiredLongValidator : Validator<Long> {

    override fun validate(validatedFieldValue: Long?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null) {
            return false
        }

        return validatedFieldValue > 0
    }

    override val reason: Resource = R.validator_required_field
}