package eu.automateeverything.alarmplugin

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.domain.configurable.StringFieldBuilder
import eu.automateeverything.domain.configurable.Validator

class ContactTypeField(name: String, hint: Resource, vararg validators: Validator<String?>) :
    FieldDefinition<String>(
        FieldType.SingleOptionEnumeration, name, hint, 0, VALUE_NC, String::class.java,
        StringFieldBuilder(), null, mapOf(Pair(VALUE_NO, R.value_no), Pair(VALUE_NC, R.value_nc)), *validators) {

    companion object {
        const val VALUE_NO = "no"
        const val VALUE_NC = "nc"
    }
}