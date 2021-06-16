package eu.geekhome.domain.configurable

import eu.geekhome.domain.R
import eu.geekhome.domain.localization.Resource

class IPAddressValidator : Validator<String> {

    override val reason: Resource = R.validator_invalid_ip_address

    override fun validate(fieldValue: String?): Boolean {
        return try {
            if (fieldValue == null || fieldValue.isEmpty()) {
                return false
            }
            val parts = fieldValue.split(".").toTypedArray()
            if (parts.size != 4) {
                return false
            }
            for (s in parts) {
                val i = s.toInt()
                if (i < 0 || i > 255) {
                    return false
                }
            }
            !fieldValue.endsWith(".")
        } catch (nfe: NumberFormatException) {
            false
        }
    }
}