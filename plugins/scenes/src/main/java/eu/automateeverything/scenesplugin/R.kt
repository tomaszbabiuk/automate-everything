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

package eu.automateeverything.scenesplugin

import eu.automateeverything.data.localization.Resource

object R {
    val plugin_name = Resource(
        "Scenes",
        "Sceny"
    )

    val plugin_description = Resource(
        "Scene objects that helps share behaviors/automations across objects",
        "Obiekty \"Scen\", które są pomocne przy współdzieleniu zachować/automatyki pomiędzy różnymi obiektami",
    )
    val field_automation_only_hint = Resource(
        "Automation control only",
        "Kontrola tylko przez automatykę"
    )
    
    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )

    val state_active = Resource(
        "Active",
        "Aktywne"
    )

    val state_inactive = Resource(
        "Inactive",
        "Nieaktywne"
    )

    val action_activate = Resource(
        "Activate",
        "Aktywuj"
    )

    val action_deactivate = Resource(
        "Deactivate",
        "Dezaktywuj"
    )

    val configurable_scene_add = Resource(
        "Add scene",
        "Dodaj scenę"
    )

    val configurable_scene_edit = Resource(
        "Edit scene",
        "Edytuj scenę"
    )

    val configurable_scene_title = Resource(
        "Scenes",
        "Sceny"
    )

    val configurable_scene_description = Resource(
        "Scene is a set of conditions that define specyfic behavior",
        "Sceny to zbiór warunków, które definiują określone zachowanie"
    )
}