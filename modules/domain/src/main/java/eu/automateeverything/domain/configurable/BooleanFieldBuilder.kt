package eu.automateeverything.domain.configurable

class BooleanFieldBuilder : FieldBuilder<Boolean> {

    override fun fromPersistableString(value: String?): Boolean {
        return value == "1"
    }

    override fun toPersistableString(value: Boolean): String {
        return if (value) {
            "1"
        } else {
            "0"
        }
    }
}