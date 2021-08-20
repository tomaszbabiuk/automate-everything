package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

data class FieldValidationResult(
    val valid: Boolean,
    val reasons: List<Resource>
)