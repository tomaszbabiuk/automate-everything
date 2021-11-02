package eu.automateeverything.domain.configurable

interface FieldBuilder<T> {
    fun fromPersistableString(value: String?): T
    fun toPersistableString(value: T) : String
}

class ParsingFieldException(clazz: Class<*>, value: String?) :
    Exception("There's been problem with parsing ${clazz.simpleName} field from value $value")