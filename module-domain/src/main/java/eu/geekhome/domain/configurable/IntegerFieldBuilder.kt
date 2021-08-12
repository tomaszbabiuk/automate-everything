package eu.geekhome.domain.configurable

class IntegerFieldBuilder : FieldBuilder<Int> {
    override fun fromPersistableString(value: String?): Int {
        if (value == null) {
            return 0
        }

        return value.toInt()
    }

    override fun toPersistableString(value: Int): String {
        return value.toString()
    }
}