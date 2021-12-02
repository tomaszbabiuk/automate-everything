package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class BigDecimalField(
    name: String,
    hint: Resource,
    maxSize: Int,
    initialValue: BigDecimal,
    vararg validators: Validator<NullableBigDecimal?>) :
    FieldDefinition<NullableBigDecimal>(
        FieldType.BigDecimal, name, hint, maxSize, NullableBigDecimal(initialValue), NullableBigDecimal::class.java,
        NullableBigDecimalFieldBuilder(), null, null, *validators
    )