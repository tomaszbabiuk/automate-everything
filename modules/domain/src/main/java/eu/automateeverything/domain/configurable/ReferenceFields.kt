package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.fields.PortReference
import eu.automateeverything.data.fields.InstanceReference
import eu.automateeverything.data.fields.PortReferenceType
import eu.automateeverything.data.fields.Reference
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.hardware.*

open class ReferenceField<R : Reference>(type: FieldType, name: String, hint: Resource, ref: R, vararg validators: Validator<String?>) :
    FieldDefinition<String>(type, name, hint, 0, "",
        String::class.java, StringFieldBuilder(), ref, null, *validators)

open class PortReferenceField(name: String, hint: Resource, ref: PortReference, vararg validators: Validator<String?>) :
    ReferenceField<PortReference>(FieldType.PortReference, name, hint, ref, *validators)

open class InstanceReferenceField(name: String, hint: Resource, ref: InstanceReference, vararg validators: Validator<String?>) :
    ReferenceField<InstanceReference>(FieldType.InstanceReference, name, hint, ref, *validators)

class WattageInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint,  PortReference(Wattage::class.java, PortReferenceType.Input), *validators)

class BinaryInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint,  PortReference(BinaryInput::class.java, PortReferenceType.Input), *validators)

class RelayOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint, PortReference(Relay::class.java, PortReferenceType.Output), *validators)

class PowerLevelOutputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint, PortReference(PowerLevel::class.java, PortReferenceType.Output), *validators)

class TemperatureInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint, PortReference(Temperature::class.java, PortReferenceType.Input), *validators)

class HumidityInputPortField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    PortReferenceField(name, hint, PortReference(Humidity::class.java, PortReferenceType.Input), *validators)
