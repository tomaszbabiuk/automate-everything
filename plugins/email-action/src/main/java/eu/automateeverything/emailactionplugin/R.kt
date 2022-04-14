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

object R {
    val field_recipient_hint = Resource(
        "Recipient",
        "Odbiorca"
    )

    val field_subject_hint = Resource(
        "Subject",
        "Subject"
    )

    val field_body_hint = Resource(
        "Body",
        "Treść"
    )

    val field_username = Resource(
        "Username",
        "Nazwa użytkownika"
    )

    val field_password = Resource(
        "Password",
        "Hasło"
    )

    val field_host = Resource(
        "Host address",
        "Adres hosta"
    )

    val smtp_settings_title = Resource(
        "SMTP configuration",
        "Ustawienia SMTP"
    )

    val smtp_settings_description = Resource(
        "SMTP server parameters",
        "Parametry serwera SMTP"
    )

    val configurable_email_action_edit = Resource(
        "Edit e-mail action",
        "Edytuj akcję e-mail"
    )
    val configurable_email_action_add = Resource(
        "Add e-mail action",
        "Dodaj akcję e-mail"
    )

    val plugin_name = Resource(
        "E-mail action",
        "Akcja e-mail"
    )

    val plugin_description = Resource(
        "Contains e-mail sending action",
        "Zawiera akcję pozwalającą na wysyłanie poczty elektronicznej"
    )

    val configurable_email_action_title = Resource(
        "E-mail action",
        "Akcja e-mail"
    )

    val configurable_email_action_description = Resource(
        "Sends an e-mail",
        "Wysyła e-mail"
    )

    val error_no_settings = Resource(
        "SMTP settings are not defined. Edit the settings of 'E-mail action' plugin and fill missing data.",
        "Ustawienia servera SMTP nie są zdefiniowane. Edytuj ustawienia rozszerzenia 'Akcja e-mail' i wprowadż wymagane dane."
    )

    val error_host_not_defined = Resource(
        "SMTP 'host' not defined! You can edit this in the settings of 'E-mail action' plugin",
        "Adres 'hosta' SMTP nie jest zdefiniowany. Można go wprowadzić w ustawieniach rozszerzenia 'Akcja e-mail'"
    )

    val error_username_not_defined = Resource(
        "SMTP 'username' not defined! You can edit this in the settings of 'E-mail action' plugin",
        "'Nazwa użytkownika' SMTP nie jest zdefiniowana. Można ją wprowadzić w ustawieniach rozszerzenia 'Akcja e-mail'"
    )

    val error_password_not_defined = Resource(
        "SMTP 'username' not defined! You can edit this in the settings of 'E-mail action' plugin",
        "'Hasło' SMTP nie jest zdefiniowane. Można je wprowadzić w ustawieniach rozszerzenia 'Akcja e-mail'"
    )
}