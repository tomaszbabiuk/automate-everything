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

package eu.automateeverything.onoffplugin

import eu.automateeverything.data.localization.Resource

object R {
    val plugin_description = Resource(
        "On/Off devices and power regulators",
        "Urządzenia włącz/wyłącz oraz regulatory mocy"
    )

    val plugin_name = Resource(
        "On/Off devices",
        "Urzączenia Wł/Wył"
    )

    val configurable_onoff_devices_title = Resource(
        "On/Off",
        "Wł/Wył"
    )

    val configurable_onoff_devices_description = Resource(
        "Devices that can by controlled by relays, mosfets, etc.",
        "Urządzenia sterowane przekaźnikami, tranzystorami mocy, itp."
    )

    val configurable_power_regulators_title = Resource(
        "Power regulators",
        "Regulatory mocy"
    )

    val configurable_power_regulators_description = Resource(
        "Power regulators are devices that can control its power from 0-100%",
        "Regulatory mocy to urządzenia, których moc może być regulowana w zakresie 0-100%"
    )

    val configurable_power_regulator_add = Resource(
            "Add power regulator",
            "Dodaj regulator mocy"
    )

    val configurable_power_regulator_edit = Resource(
        "Edit power regulator",
        "Edytuj regulator mocy"
    )

    val validator_max_should_exceed_min_time = Resource(
        "Maximum working time should be greater than minimum working time",
        "Maksymalny czas pracy powinien być większy niż minimalny czas pracy"
    )

    val validator_break_invalid_if_no_max_time = Resource(
        "Break time is not effective when max working time is zero",
        "Czas przerwy jest nieskuteczny jeśli maksymalny czas pracy jest zerowy"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val field_automation_only_hint = Resource(
        "Automation control only",
        "Kontrola tylko przez automatykę"
    )

    val field_min_working_time = Resource(
        "Minimum working time",
        "Minimalny czas pracy "
    )

    val field_max_working_time = Resource(
        "Maximum working time (00:00:00 = infinity)",
        "Maksymalny czas pracy (00:00:00 = nieskończoność)"
    )

    val field_break_time = Resource(
        "Break time (after max. time elapses)",
        "Czas przerwy (po upłynięciu maksymalnego czasu)"
    )

    val configurable_onoffdevice_add = Resource(
        "Add on/off device",
        "Dodaj urządzenie wł/wył"
    )

    val configurable_onoffdevice_edit = Resource(
        "Edit on/off device",
        "Edytuj urządzenie wł/wył"
    )

    val configurable_onoffdevice_title = Resource(
        "On/Off devices",
        "Urządzenia wł/wył"
    )

    val configurable_onoffdevices_description = Resource(
        "A very simple to automate on/off devices",
        "Proste w automatyzowaniu urządzenia typu: włącz/wyłącz"
    )

    val configurable_timedonoffdevice_add = Resource(
        "Add timed on/off device",
        "Dodaj czasowe urządzenie wł/wył"
    )

    val configurable_timedonoffdevice_edit = Resource(
        "Edit timed on/off device",
        "Edytuj czasowe urządzenie wł/wył"
    )

    val configurable_timedonoffdevice_title = Resource(
        "Timed On/Off devices",
        "Urządzenia wł/wył z funkcją czasu"
    )

    val configurable_timedonoffdevices_description = Resource(
        "More advanvced on/off devices with the possibility of setting min/max/brake times",
        "Bardziej zaawansowane urządzenia z możliwością zdefiniowania czasów minimalnej i maksymalnej pracy oraz przerwy"
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )

    val state_on = Resource(
        "On",
        "Wł"
    )

    val action_on = Resource(
        "On",
        "Wł"
    )

    val state_on_counting = Resource(
        "On (counting)",
        "Wł (odliczanie)"
    )

    val state_off = Resource(
        "Off",
        "Wył"
    )

    val action_off = Resource(
        "Off",
        "Wył"
    )

    val state_forced_off = Resource(
        "Forced off",
        "Wymuszone wył"
    )

    val state_off_break = Resource(
        "Off (break)",
        "Wył (przerwa)"
    )
}