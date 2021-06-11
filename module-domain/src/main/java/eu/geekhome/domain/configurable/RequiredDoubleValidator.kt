package eu.geekhome.domain.configurable

import eu.geekhome.domain.localization.Resource

class RequiredDoubleValidator : Validator<Double?> {
    override val reason: Resource
        get() = Resource(
            "This field is required",
            "To pole jest wymagane"
        )

    override fun validate(fieldValue: Double?): Boolean {
        return fieldValue != null
    }
}