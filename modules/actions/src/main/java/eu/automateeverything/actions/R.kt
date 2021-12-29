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

package eu.automateeverything.actions

import eu.automateeverything.data.localization.Resource

object R {

    val state_ready = Resource(
        "Ready",
        "Gotowe"
    )

    val action_execute = Resource(
        "Execute",
        "Wykonaj"
    )

    val state_executing = Resource(
        "Executing",
        "Wykonywanie"
    )

    val state_success = Resource(
        "Success",
        "Sukces"
    )

    val state_cancelled = Resource(
        "Cancelled",
        "Anulowano"
    )

    val state_failure = Resource(
        "Failure",
        "Porażka"
    )

    val action_reset = Resource(
        "Reset",
        "Resetuj"
    )

    val action_cancel = Resource(
        "Cancel",
        "Anuluj"
    )

    val configurable_actions_title = Resource(
        "Actions",
        "Akcje"
    )

    val configurable_actions_description = Resource(
        "Add/remove actions (sending messages, calling other systems, executing bash scripts, etc.)",
        "Dodawaj/usuwaj akcje (np. wysyłanie wiadomości, uruchamianie skryptów, wywoływanie innych systemów itp.)"
    )

}