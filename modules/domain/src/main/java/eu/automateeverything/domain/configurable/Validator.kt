package eu.automateeverything.domain.configurable

import eu.automateeverything.data.localization.Resource

interface Validator<T> {
    val reason: Resource
    fun validate(validatedFieldValue: T?, allFields: Map<String, String?>): Boolean
}