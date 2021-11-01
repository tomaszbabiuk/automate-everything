package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource

class BooleanField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: Boolean,
    vararg validators: Validator<Boolean?>
) : FieldDefinition<Boolean>(
    FieldType.Boolean, name, hint, maxSize, initialValue, Boolean::class.java,
    BooleanFieldBuilder(), null, null, *validators)