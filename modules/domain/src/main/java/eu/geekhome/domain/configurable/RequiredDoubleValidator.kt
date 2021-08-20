package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class RequiredDoubleValidator : Validator<Double?> {
    override val reason: Resource
        get() = Resource(
            "This field is required",
            "To pole jest wymagane"
        )

    override fun validate(validatedFieldValue: Double?, allFields: Map<String, String?>): Boolean {
        return validatedFieldValue != null
    }
}