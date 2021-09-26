package eu.automateeverything.domain.configurable

class StringFieldBuilder : FieldBuilder<String> {
    override fun fromPersistableString(value: String?): String {
        if (value == null) {
            return ""
        }

        return value
    }

    override fun toPersistableString(value: String): String {
        return value
    }
}