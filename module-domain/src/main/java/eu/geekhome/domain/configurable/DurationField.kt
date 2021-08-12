package eu.geekhome.domain.configurable

import eu.geekhome.data.localization.Resource

class DurationField(name: String, hint: Resource, vararg validators: Validator<Duration>) :
    FieldDefinition<Duration>(name, hint, 0, Duration::class.java, DurationFieldBuilder(), *validators)