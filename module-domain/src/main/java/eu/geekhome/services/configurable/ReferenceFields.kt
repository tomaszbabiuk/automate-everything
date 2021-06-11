package eu.geekhome.services.configurable

import eu.geekhome.services.localization.Resource

open class ReferenceField(name: String, hint: Resource, vararg validators: Validator<String>) :
    FieldDefinition<String>(name, hint, 0, String::class.java, StringFieldBuilder(), *validators)

class WattageReadPortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class WattageWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class WattageReadWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)

class RelayReadPortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class RelayWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class RelayReadWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)

class TemperatureReadPortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class TemperatureWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class TemperatureReadWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)

class HumidityReadPortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class HumidityWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
class HumidityReadWritePortField(name: String, hint: Resource, vararg validators: Validator<String>) : ReferenceField(name, hint, *validators)
