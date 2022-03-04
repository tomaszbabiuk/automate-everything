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

import eu.automateeverything.data.localization.Resource

object R {
    val mqtt_settings_title = Resource(
        "MQTT broker settings",
        "Ustawienia brokera MQTT"
    )

    val mqtt_settings_description = Resource(
        "The MQTT broker is a communication proxy between Automate-Everything server and mobile clients (Android, iOS).",
        "Broker MQTT pośredniczy w wymianie danych pomiędzy serverem Automate-Everything i klientami mobilnymi (Android, iOS)."
    )

    val field_username = Resource(
        "Username",
        "Nazwa użytkownika"
    )

    val field_password = Resource(
        "Password",
        "Hasło"
    )

    val inbox_message_mqtt_server_error_missing_keypair = Resource(
        "Connection error. Missing key-pair that's used to cipher the data channel!",
        "Błąd połączenia. Para kluczy do szyfrowania kanału danych została usunięta!"
    )

    val inbox_message_mqtt_server_error_subject = Resource(
        "Mobile access plugin error",
        "Błąd pluginu 'Dostęp zdalny'"
    )

    val inbox_message_broker_disconnected = Resource(
        "Cannot connect to the MQTT broker. The system will auto-retry every two minutes.",
        "Nie można uzyskać połączenia z brokerem MQTT. System będzie próbował ponownie co dwie minuty."
    )

    val inbox_message_broker_connected = Resource(
        "The connection to the MQTT broker has been established.",
        "Połączenie z brokerem MQTT zostało ustabilizowane."
    )

    val secrets_protection_title = Resource(
        "Secrets protection",
        "Ochrona haseł"
    )

    val secrets_protection_description = Resource(
        "Password for protecting access secrets. Note that you'll need to remove all existing mobile credentials if the password will change. The new password will be applied to new credentials only.",
        "Hasło chroniące klucze dostępu. Wszystkie istniejące poświadczenia mobilne przestaną działać i będą musiały zostać usunięte. Nowe hasło będzie stosowane tylko do nowych poświadczeń."
    )

    val field_password_hint = Resource(
        "Password",
        "Hasło"
    )

    val field_mqtt_broker_address = Resource(
        "MQTT broker address",
        "Adres brokera MQTT"
    )

    val field_server_public_key = Resource(
        "Public key of the server",
        "Klucz publiczny serwera"
    )

    val field_client_public_key = Resource(
        "Public key of the client",
        "Klucz publiczny klienta"
    )

    val field_activated_hint= Resource(
        "Activated",
        "Aktywowany"
    )

    val field_invitation_hint = Resource(
        "Scan this QR code with your smartphone and follow the instructions to activate this channel",
        "Zeskanuj kod telefonem i podążaj za instrukcjami aby aktywawać kanał"
    )

    val mobile_credentials_title = Resource(
        "Mobile credentials",
        "Poświadczenia mobilne"
    )

    val mobile_credentials_description = Resource(
        "Contains a list of allowed mobile credentials (of devices that can connect remotely).",
        "Zawiera listę poświadczeń mobilnych (urządzeń, które mogą się łączyć zdalnie)."
    )

    var plugin_description = Resource(
        "Allows to control devices from an Android mobile phone. The connection is secured by SaltChannel.",
        "Pozwala na kontrolowanie urządzeń za pomocą smartfona z Androidem. Połączenie jest zabezpieczone przez SaltChannel"
    )

    var plugin_name = Resource("Mobile access", "Dostęp mobilny")
}