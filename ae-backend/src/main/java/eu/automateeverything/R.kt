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

package eu.automateeverything

import eu.automateeverything.data.localization.Resource

@Suppress("FunctionName")
object R {
    val plugin_no_name = Resource(
        "(no name)",
        "(brak nazwy)"
    )

    val plugin_no_description = Resource(
        "(no description)",
        "(brak opisu)"
    )

    val automation_history_automation = Resource(
        "Automation",
        "Automatyka"
    )

    val automation_history_enabled = Resource(
        "Enabled",
        "Włączona"
    )
    val automation_history_disabled = Resource(
        "Disabled",
        "Wyłączona"
    )

    val inbox_custom_message_subject = Resource(
        "New message",
        "Nowa wiadomość"
    )

    val inbox_custom_message_empty = Resource(
        "(Empty message)",
        "(Pusta wiadomość)"
    )

    val inbox_message_welcome_subject = Resource(
        "Welcome. Thank you for choosing Automate Everything.",
        "Witaj. Dziękujemy za wybór Automate Everything. "
    )

    val inbox_message_welcome_description_body = Resource(
        "What are you going to automate today? A smart home... a green house... or maybe a plant watering system? Start from enabling best plugins for the job.",
        "Co zamierzasz dzisiaj zautomatyzować? Inteligentny dom... szklarnię... a może system podlewania roślin? Zacznij od włączenia pluginów najbardziej pasujących do wykonania zadania."
    )

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
}
