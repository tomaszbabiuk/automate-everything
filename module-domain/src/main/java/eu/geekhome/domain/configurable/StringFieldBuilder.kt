package eu.geekhome.domain.configurable

class StringFieldBuilder : FieldBuilder<String?> {
    override fun fromPersistableString(value: String?): String {
        if (value == null) {
            return ""
        }

        return value
    }
}