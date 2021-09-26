package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource

class DoubleField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: Double,
    vararg validators: Validator<Double?>) :
    FieldDefinition<Double>(
        FieldType.Double, name, hint, maxSize, initialValue, Double::class.java, DoubleFieldBuilder(), null, *validators
    )