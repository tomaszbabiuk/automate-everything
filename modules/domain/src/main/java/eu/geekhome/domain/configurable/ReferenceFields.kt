package eu.geekhome.domain.configurable

import eu.geekhome.data.fields.FieldType
import eu.geekhome.data.fields.Reference
import eu.geekhome.data.fields.ReferenceType
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.hardware.*

open class ReferenceField(name: String, hint: Resource, ref: Reference, vararg validators: Validator<String?>) :
    FieldDefinition<String>(FieldType.Reference, name, hint, 0, "", String::class.java, StringFieldBuilder(), ref, *validators)

class WattageInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    ReferenceField(name, hint,  Reference(Wattage::class.java, ReferenceType.InputPort), *validators)

class RelayOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    ReferenceField(name, hint, Reference(Relay::class.java, ReferenceType.OutputPort), *validators)

class PowerLevelOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    ReferenceField(name, hint, Reference(PowerLevel::class.java, ReferenceType.OutputPort), *validators)

class TemperatureInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    ReferenceField(name, hint, Reference(Temperature::class.java, ReferenceType.InputPort), *validators)

class HumidityInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    ReferenceField(name, hint, Reference(Humidity::class.java, ReferenceType.InputPort), *validators)
