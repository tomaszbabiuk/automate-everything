package eu.geekhome.domain.configurable

import eu.geekhome.data.fields.FieldType
import eu.geekhome.data.localization.Resource

class StringField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: String,
    vararg validators: Validator<String?>
) : FieldDefinition<String>(
    FieldType.String, name, hint, maxSize, initialValue, String::class.java,
    StringFieldBuilder(), null, *validators)