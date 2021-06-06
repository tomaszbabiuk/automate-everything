package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

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