package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource

class StringField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: String,
    vararg validators: Validator<String?>
) : FieldDefinition<String>(
    FieldType.String, name, hint, maxSize, initialValue, String::class.java,
    StringFieldBuilder(), null, null, *validators)