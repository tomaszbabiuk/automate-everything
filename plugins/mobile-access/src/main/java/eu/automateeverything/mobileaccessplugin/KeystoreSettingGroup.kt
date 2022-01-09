/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.domain.configurable.*

class KeystoreSettingGroup : SettingGroup {

    override val titleRes = R.keystore_settings_title
    override val descriptionRes = R.keystore_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_PASSWORD, PasswordStringField(FIELD_PASSWORD, R.field_password_hint, 0, "change-me", RequiredStringValidator())),
    )

    companion object {
        const val FIELD_PASSWORD = "password"
    }
}