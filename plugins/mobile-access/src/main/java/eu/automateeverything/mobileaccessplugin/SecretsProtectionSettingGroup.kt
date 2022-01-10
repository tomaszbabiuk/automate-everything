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

package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.domain.configurable.*

class SecretsProtectionSettingGroup : SettingGroup {

    override val titleRes = R.secrets_protection_title
    override val descriptionRes = R.secrets_protection_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_PASSWORD, PasswordStringField(FIELD_PASSWORD, R.field_password_hint, 0, DEFAULT_PASSWORD, RequiredStringValidator())),
        Pair(FIELD_MQTT_BROKER_ADDRESS, StringField(FIELD_MQTT_BROKER_ADDRESS, R.field_mqtt_broker_address, 0, DEFAULT_MQTT_BROKER_ADDRESS, RequiredStringValidator()))
    )

    companion object {
        const val FIELD_PASSWORD = "password"
        const val FIELD_MQTT_BROKER_ADDRESS = "broker-address"
        const val DEFAULT_PASSWORD = "change-me"
        const val DEFAULT_MQTT_BROKER_ADDRESS = "tcp://localhost:1883"
    }
}