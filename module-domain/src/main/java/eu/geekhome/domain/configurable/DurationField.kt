package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class DurationField(
    name: String,
    hint: Resource,
    initialValue: Duration,
    vararg validators: Validator<Duration?>
) : FieldDefinition<Duration>(name, hint, 0, initialValue,
    Duration::class.java, DurationFieldBuilder(), *validators)