package eu.automateeverything.domain.configurable

import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class GreaterThanZeroValidator : Validator<NullableBigDecimal?> {
    override val reason: Resource
        get() = Resource(
            "The value should be greater than zero",
            "Wartość powinna być dodatnia"
        )

    override fun validate(validatedFieldValue: NullableBigDecimal?, allFields: Map<String, String?>): Boolean {
        return validatedFieldValue?.wrapped != null && validatedFieldValue.wrapped >= BigDecimal.ZERO
    }
}