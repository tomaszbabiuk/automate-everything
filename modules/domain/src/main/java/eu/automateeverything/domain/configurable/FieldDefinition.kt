package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.fields.Reference
import eu.automateeverything.data.localization.Resource

abstract class FieldDefinition<T> protected constructor(
    val type: FieldType,
    val name: String,
    val hint: Resource,
    val maxSize: Int,
    private val initialValue: T,
    val valueClazz: Class<T>,
    val builder: FieldBuilder<T>,
    val reference: Reference? = null,
    private vararg val validators: Validator<T?>
) {
    fun validate(valueAsString: String?, fields: Map<String, String?>): FieldValidationResult {
        var isFieldValid = true
        val failingReasons: MutableList<Resource> = ArrayList()
        val value = builder.fromPersistableString(valueAsString)
        for (validator in validators) {
            val isValid = validator.validate(value, fields)
            if (!isValid) {
                isFieldValid = false
                failingReasons.add(validator.reason)
            }
        }
        return FieldValidationResult(isFieldValid, failingReasons)
    }

    fun initialValueAsString() : String {
        return builder.toPersistableString(initialValue)
    }
}