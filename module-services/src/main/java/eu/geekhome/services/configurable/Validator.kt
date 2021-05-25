package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

interface Validator<T> {
    val reason: Resource
    fun validate(fieldValue: T): Boolean
}