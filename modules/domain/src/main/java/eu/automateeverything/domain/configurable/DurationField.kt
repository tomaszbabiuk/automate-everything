package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource

class DurationField(
    name: String,
    hint: Resource,
    initialValue: Duration,
    vararg validators: Validator<Duration?>
) : FieldDefinition<Duration>(
    FieldType.Duration, name, hint, 0, initialValue,
    Duration::class.java, DurationFieldBuilder(), null, *validators)