package eu.automateeverything.domain.configurable

import eu.automateeverything.data.Repository
import eu.automateeverything.data.localization.Resource

class UniqueValueAcrossItsTypeValidator(
    private val clazz: Class<out Configurable>,
    private val repository: Repository,
    private val uniqueFieldName: String
) : Validator<String> {
    override val reason: Resource
        get() = Resource("Name not unique", "Nazwa nie jest unikalna")

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null || validatedFieldValue == "") {
            return false
        }

        val existingDialogs =
            repository
                .getAllInstances()
                .filter { it.clazz == clazz.name }
                .filter { it.fields[uniqueFieldName] == validatedFieldValue }

        return existingDialogs.isEmpty()
    }
}
