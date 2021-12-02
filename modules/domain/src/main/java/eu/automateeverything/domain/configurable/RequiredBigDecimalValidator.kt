package eu.automateeverything.domain.configurable

import eu.automateeverything.data.localization.Resource

class RequiredBigDecimalValidator : Validator<NullableBigDecimal?> {
    override val reason: Resource
        get() = Resource(
            "This field is required",
            "To pole jest wymagane"
        )

    override fun validate(validatedFieldValue: NullableBigDecimal?, allFields: Map<String, String?>): Boolean {
        return validatedFieldValue?.wrapped != null
    }
}