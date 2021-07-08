package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

interface Validator<T> {
    val reason: Resource
    fun validate(fieldValue: T?): Boolean
}