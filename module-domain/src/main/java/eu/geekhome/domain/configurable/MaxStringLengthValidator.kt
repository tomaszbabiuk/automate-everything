package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class MaxStringLengthValidator(val maxLength: Int) : Validator<String?> {
    override val reason: Resource
        get() = Resource(
            "Max length is $maxLength characters",
            "Maksymalna długość to $maxLength znaków"
        )

    override fun validate(fieldValue: String?, fields: Map<String, String?>): Boolean {
        return if (fieldValue == null) {
            true
        } else fieldValue.length <= maxLength
    }
}