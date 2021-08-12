package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class PowerLevelField(name: String, hint: Resource, vararg validators: Validator<Int>) :
    FieldDefinition<Int>(name, hint, 0, Int::class.java, IntegerFieldBuilder(), *validators)