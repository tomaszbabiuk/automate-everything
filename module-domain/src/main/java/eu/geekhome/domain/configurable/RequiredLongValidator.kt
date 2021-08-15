package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.data.localization.Resource

class RequiredLongValidator : Validator<Long> {

    override fun validate(fieldValue: Long?, fields: Map<String, String?>): Boolean {
        if (fieldValue == null) {
            return false
        }

        return fieldValue > 0
    }

    override val reason: Resource = R.validator_required_field
}