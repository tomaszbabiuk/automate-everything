package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import java.util.HashMap

abstract class NameDescriptionConfigurable : ConfigurableWithFields {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap()
            result[FIELD_NAME] = nameField
            result[FIELD_DESCRIPTION] = descriptionField
            return result
        }

    private val nameField = StringField(
        FIELD_NAME, R.field_name_hint, 50,
        RequiredStringValidator(), MaxStringLengthValidator(50)
    )

    private val descriptionField = StringField(
        FIELD_DESCRIPTION, R.field_description_hint, 200,
        MaxStringLengthValidator(200)
    )

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
    }
}