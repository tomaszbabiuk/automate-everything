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

@file:Suppress("FunctionName")

package eu.automateeverything.domain

import eu.automateeverything.data.localization.Resource
import kotlin.Exception

object R {
    val inbox_message_automation_enabled_subject = Resource(
        "Automation has been enabled",
        "Automatyka została włączona"
    )

    val inbox_message_automation_enabled_body = Resource(
        "Automation is ON.",
        "Automatyka została uruchomiona."
    )

    val inbox_message_automation_disabled_subject = Resource(
        "Automation has been disabled",
        "Automatyka została wyłączona"
    )

    val inbox_message_automation_disabled_body = Resource(
        "Automation loops are now OFF",
        "Pętle automatyki zostały wstrzymane"
    )

    val inbox_message_new_port_found_subject = Resource(
        "New port has been found!",
        "Znaleziono nowy port."
    )

    fun inbox_message_port_found_body(newPortId: String?) = Resource (
        "It's unique ID is $newPortId",
        "Jego oznaczenie to $newPortId"
    )

    var category_this_object = Resource(
        "This object",
        "Ten obiekt"
    )

    var category_power_level = Resource(
        "Power",
        "Moc"
    )

    val category_triggers_conditions = Resource(
        "Conditions",
        "Warunki"
    )

    val category_triggers = Resource(
        "Triggers",
        "Wyzwalacze"
    )

    val category_logic = Resource(
        "Logic",
        "Logika"
    )

    val category_temperature = Resource(
        "Temperature",
        "Temperatura"
    )

    val category_state = Resource(
        "State",
        "Stan"
    )

    val category_wattage = Resource(
        "Wattage",
        "Waty"
    )

    val category_humidity = Resource(
        "Humidity",
        "Wilgotność"
    )

    val category_luminosity = Resource(
        "Luminosity",
        "Jasność"
    )

    val block_label_delay = Resource(
        "Wait for %1 seconds %2 and than: %3",
        "Poczekaj %1 sekund %2 potem: %3"
    )

    val block_label_if_than_else = Resource(
        "If %1 than %2 else %3",
        "Jeżeli %1 to %2 inaczej %3"
    )

    val block_label_and = Resource(
        "%1 and %2 %3",
        "%1 i %2 %3"
    )

    val block_label_or = Resource(
        "%1 or %2 %3",
        "%1 lub %2 %3"
    )

    val block_label_not = Resource(
        "not %1 %2",
        "nie %1 %2"
    )

    val block_label_repeat = Resource(
        "Repeat every %1",
        "Powtarzaj co %1"
    )

    val block_label_changes_state = Resource(
        "%1 changes state to %2",
        "%1 zmienia stan na %2"
    )

    val block_label_change_value = Resource(
        "Change defaults to %1 %2",
        "Zmień domyślne ustawienie na%1 %2"
    )

    val block_label_in_state = Resource(
        "%1 is in state %2",
        "%1 jest w stanie %2"
    )

    val second = Resource(
        "second",
        "sekundę"
    )

    val seconds15 = Resource(
        "15 seconds",
        "15 sekund"
    )

    val seconds30 = Resource(
        "30 seconds",
        "30 sekund"
    )

    val minute = Resource(
        "minute",
        "minutę"
    )

    val minutes2 = Resource(
        "2 minutes",
        "2 minuty"
    )

    val minutes5 = Resource(
        "5 minutes",
        "5 minut"
    )

    val minutes10 = Resource(
        "10 minutes",
        "10 minut"
    )

    val minutes30 = Resource(
        "30 minutes",
        "30 minut"
    )

    val hour = Resource(
        "hour",
        "godzinę"
    )

    val field_name_hint = Resource(
        "Name",
        "Nazwa"
    )

    val field_description_hint = Resource(
        "Description",
        "Opis"
    )

    val error_automation = Resource(
        "Automation error",
        "Błąd automatyki"
    )

    fun error_automation(cause: Exception) = Resource(
        "Automation error. Detailed error is: ${cause.message}",
        "Błąd automatyki. Szczegóły błędu: ${cause.message}"
    )

    fun error_device_missing(clazz: String) = Resource(
        "Automation error. Device of class $clazz is missing! Version mismatch or the plugin defining this device is disabled!",
        "Błąd automatyki. Urządzenie o klasie $clazz nie istnieje. Może to wynikać z niezgodności wersji, lub plugin który definiuje to urządzenie jest wyłączony!"
    )

    fun error_port_not_found(portId: String): Resource {
        return Resource(
            "Port $portId not found or disconnected",
            "Nie odnaleziono portu $portId lub port został rozłączony"
        )
    }

    val error_initialization = Resource(
        "Initialization error",
        "Błąd inicjalizacji"
    )

    fun error_other_device_failure(unknownName: String) = Resource(
        "Automation of this device uses another device ($unknownName) that's not available",
        "Automatyka tego urządzenia używa innego urządzenia ($unknownName), które jest niedostępne"
    )
    
    val validator_required_field = Resource(
        "This field is required",
        "To pole jest wymagane"
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )
}