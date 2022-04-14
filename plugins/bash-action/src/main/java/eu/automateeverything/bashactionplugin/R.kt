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

package eu.automateeverything.bashactionplugin

import eu.automateeverything.data.localization.Resource

object R {
    val field_command_hint = Resource(
        "Bash command to execute",
        "Komenda bash do wykonania"
    )

    val configurable_bash_script_action_title = Resource(
        "Bash script",
        "Skrypt bash"
    )

    val configurable_bash_script_action_description = Resource(
        "Executes bash script",
        "Uruchamia skrypt bash"
    )

    val configurable_bash_script_action_add = Resource(
        "Add bash script action",
        "Dodaj skrypt bash"
    )

    val configurable_bash_script_action_edit = Resource(
        "Edit bash script",
        "Edytuj skrypt bash"
    )

    val plugin_name = Resource(
        "Bash action",
        "Akcja bash (komenda)"
    )

    val plugin_description = Resource(
        "Calls bash action",
        "Wywołuje akcję systemową"
    )
}