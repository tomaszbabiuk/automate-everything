package eu.geekhome.domain.configurable

import eu.geekhome.data.fields.FieldType
import eu.geekhome.data.localization.Resource

class BooleanField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: Boolean,
    vararg validators: Validator<Boolean?>
) : FieldDefinition<Boolean>(
    FieldType.Boolean, name, hint, maxSize, initialValue, Boolean::class.java,
    BooleanFieldBuilder(), null, *validators)