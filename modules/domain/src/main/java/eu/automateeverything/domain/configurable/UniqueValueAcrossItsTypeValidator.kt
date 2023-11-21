package eu.automateeverything.domain.configurable

import eu.automateeverything.data.DataRepository
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.NameDescriptionConfigurable.Companion.FIELD_NAME

class UniqueValueAcrossItsTypeValidator(
    private val clazz: Class<out Configurable>,
    private val dataRepository: DataRepository,
    private val uniqueFieldName: String
) : Validator<String> {
    override val reason: Resource
        get() = Resource("Name not unique", "Nazwa nie jest unikalna")

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        if (validatedFieldValue == null || validatedFieldValue == "") {
            return false
        }

        if (validatedFieldValue == allFields[FIELD_NAME]) {
            return true
        }

        val existingInstances =
            dataRepository
                .getAllInstances()
                .filter { it.clazz == clazz.name }
                .filter { it.fields[uniqueFieldName] == validatedFieldValue }

        return existingInstances.isEmpty()
    }
}
