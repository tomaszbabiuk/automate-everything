package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

open class ReferenceField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    FieldDefinition<String>(name, hint, 0, "", String::class.java, StringFieldBuilder(), *validators)

class WattageInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) : ReferenceField(name, hint, *validators)

class RelayOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) : ReferenceField(name, hint, *validators)
class PowerLevelOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) : ReferenceField(name, hint, *validators)

class TemperatureInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) : ReferenceField(name, hint, *validators)

class HumidityInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) : ReferenceField(name, hint, *validators)
