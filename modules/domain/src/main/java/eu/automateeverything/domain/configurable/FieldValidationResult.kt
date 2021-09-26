package eu.automateeverything.domain.configurable

import eu.automateeverything.data.localization.Resource

data class FieldValidationResult(
    val valid: Boolean,
    val reasons: List<Resource>
)