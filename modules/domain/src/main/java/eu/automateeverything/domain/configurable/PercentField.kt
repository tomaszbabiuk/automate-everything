package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class PercentField(
    name: String,
    hint: Resource,
    initialValue: BigDecimal,
    vararg validators: Validator<BigDecimal?>
) : FieldDefinition<BigDecimal>(
    FieldType.Percent, name, hint, 0, initialValue, BigDecimal::class.java,
    BigDecimalFieldBuilder(), null, null, *validators)