package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource
import java.util.ArrayList

abstract class FieldDefinition<T> protected constructor(
    val name: String,
    val hint: Resource,
    val maxSize: Int,
    val initialValue: T,
    val valueClazz: Class<T>,
    val builder: FieldBuilder<T>,
    private vararg val validators: Validator<T?>
) {
    fun validate(valueAsString: String?): FieldValidationResult {
        var isFieldValid = true
        val failingReasons: MutableList<Resource> = ArrayList()
        val value = builder.fromPersistableString(valueAsString)
        for (validator in validators) {
            val isValid = validator.validate(value)
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