package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class MaxStringLengthValidator(val maxLength: Int) : Validator<String?> {
    override val reason: Resource
        get() = Resource(
            "Max length is $maxLength characters",
            "Maksymalna długość to $maxLength znaków"
        )

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        return if (validatedFieldValue == null) {
            true
        } else validatedFieldValue.length <= maxLength
    }
}