package eu.geekhome.domain.configurable

import eu.geekhome.data.fields.FieldType
import eu.geekhome.data.localization.Resource

class PowerLevelField(
    name: String,
    hint: Resource,
    initialValue: Int,
    vararg validators: Validator<Int?>
) : FieldDefinition<Int>(
    FieldType.PowerLevel, name, hint, 0, initialValue, Int::class.java,
    IntegerFieldBuilder(), null, *validators)