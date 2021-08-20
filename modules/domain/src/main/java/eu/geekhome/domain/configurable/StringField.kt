package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class StringField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: String,
    vararg validators: Validator<String?>
) : FieldDefinition<String>(name, hint, maxSize, initialValue, String::class.java,
    StringFieldBuilder(), *validators)