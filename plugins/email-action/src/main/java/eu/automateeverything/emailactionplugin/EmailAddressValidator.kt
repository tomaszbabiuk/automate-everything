/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.emailactionplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.Validator
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

class EmailAddressValidator : Validator<String?> {
    override val reason: Resource
        get() = Resource(
            "The value should be greater than zero",
            "Wartość powinna być dodatnia"
        )

    override fun validate(validatedFieldValue: String?, allFields: Map<String, String?>): Boolean {
        try {
            val emailAddress = InternetAddress(validatedFieldValue)
            emailAddress.validate()
        } catch (ex: AddressException) {
            return false
        }
        return true
    }
}