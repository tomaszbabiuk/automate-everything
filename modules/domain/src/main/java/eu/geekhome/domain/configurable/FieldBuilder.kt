package eu.geekhome.domain.configurable

interface FieldBuilder<T> {
    fun fromPersistableString(value: String?): T
    fun toPersistableString(value: T) : String
}