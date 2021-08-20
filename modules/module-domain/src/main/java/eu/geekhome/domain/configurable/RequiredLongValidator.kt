package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.data.localization.Resource

class RequiredLongValidator : Validator<Long> {

    override fun validate(validatedFieldValue: Long?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null) {
            return false
        }

        return validatedFieldValue > 0
    }

    override val reason: Resource = R.validator_required_field
}