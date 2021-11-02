package eu.automateeverything.alarmplugin

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.*

enum class ContactType(val raw: String, private val translation: Resource) {
    NO("no", R.value_no),
    NC("nc", R.value_nc);

    fun toPair() : Pair<String, Resource> {
        return Pair(raw, translation)
    }
}

class ContactTypeField(name: String, hint: Resource, vararg validators: Validator<ContactType?>) :
    FieldDefinition<ContactType>(
        FieldType.SingleOptionEnumeration, name, hint, 0, ContactType.NC, ContactType::class.java,
        ContactTypeFieldBuilder(), null, mapOf(ContactType.NO.toPair(), ContactType.NC.toPair()), *validators) {
}

class ContactTypeFieldBuilder : FieldBuilder<ContactType> {
    override fun fromPersistableString(value: String?): ContactType {
        if (value == null || value.isEmpty()) {
            throw ParsingFieldException(ContactType::class.java, value)
        }

        return ContactType.values().first { it.raw == value }
    }

    override fun toPersistableString(value: ContactType): String {
        return value.raw
    }
}