package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class PowerLevelField(
    name: String,
    hint: Resource,
    initialValue: Int,
    vararg validators: Validator<Int?>
) : FieldDefinition<Int>(name, hint, 0, initialValue, Int::class.java,
    IntegerFieldBuilder(), *validators)