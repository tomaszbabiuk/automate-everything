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

package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.localization.Resource

object R {

    @Suppress("FunctionName")
    fun note_opening_level(value: Int) = Resource(
        "Valve opening: $value",
        "Otwarcie zaworu: $value"
    )

    val note_relay_state_disengaged = Resource(
        "Relay state: disengaged",
        "Stan przekaźnika: rozłączony"
    )

    val note_relay_state_engaged = Resource(
        "Relay state: engaged",
        "Stan przekaźnika: załączony"
    )

    val state_off = Resource(
        "Off",
        "Wyłączony"
    )

    val action_off = Resource(
        "Off",
        "Wyłącz"
    )

    val field_thermometer_hint = Resource(
        "Thermometer",
        "Termometr"
    )

    val state_enabled = Resource(
        "Enabled",
        "Aktywne"
    )

    val state_disabled = Resource(
        "Disabled",
        "Nieaktywne"
    )

    val action_enable = Resource(
        "Enable",
        "Aktywuj"
    )

    val action_disable = Resource(
        "Disable",
        "Deaktywuj"
    )

    val state_pumping = Resource(
        "Pumping",
        "Pompowanie"
    )

    val state_standby = Resource(
        "Standby",
        "W gotowości"
    )

    val action_standby = Resource(
        "Standby",
        "Gotowość"
    )

    val state_regulation = Resource(
        "Regulation",
        "Regulacja"
    )

    val field_thermal_actuators_hint = Resource(
        "Thermal actuators",
        "Siłowniki termiczne"
    )

    val field_minimum_working_time_hint = Resource(
        "Minimum working time",
        "Minimalny czas pracy"
    )

    val field_pump_port_hint = Resource(
        "Pump relay port",
        "Port przekaźnika pompy"
    )

    val field_transformer_port_hint = Resource(
        "Transformer port (supplying power for the actuators)",
        "Port transformatora zasilającego siłowniki termiczne"
    )

    val field_actuator_port_hint = Resource(
        "Actuator port",
        "Port uruchamiający siłownik"
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )

    val field_inactive_state_hint = Resource(
        "Inactive state",
        "Stan bezczynności"
    )

    val value_no = Resource(
        "NO (normally open)",
        "NO (normalnie otwarty)"
    )

    val value_nc = Resource(
        "NC (normally closed)",
        "NZ (normalnie zamknięty)"
    )

    val field_opening_time_hint = Resource(
        "Opening time",
        "Czas otwierania"
    )

    val configurable_central_heating_title = Resource(
        "Central Heating",
        "Centralne ogrzewanie"
    )

    val configurable_central_heating_description = Resource(
        "Devices that control central heating (thermal actuators, central heating pumps, circulation pumps, etc.)",
        "Urządzenia do sterowania centralnym ogrzewaniem (termosiłowniki, pompy c.o., pompy cyrkulacji c.w.u. itp.)"
    )

    var plugin_description = Resource(
        "Central Heating devices",
        "Urządzenia do centralnego ogrzewania"
    )

    var plugin_name = Resource("Central heating", "Centralne ogrzewanie")

    val configurable_thermal_actuator_add = Resource(
        "Add thermal actuator",
        "Dodaj siłownik termiczny"
    )

    val configurable_thermal_actuator_edit = Resource(
        "Edit thermal actuator",
        "Edytuj siłownik termiczny"
    )

    val configurable_thermal_actuators_title = Resource(
        "Thermal actuators",
        "Siłowniki termiczne"
    )

    val configurable_thermal_actuators_description = Resource(
        "Controls opening and closing of thermal actuator.",
        "Kontroluje otwieranie i zamykanie siłownika termicznego."
    )

    val configurable_central_heating_pump_add = Resource(
        "Add central heating pump",
        "Dodaj pompę c.o."
    )

    val configurable_central_heating_pump_edit = Resource(
        "Edit central heating pump",
        "Edytuj pompę c.o."
    )

    val configurable_central_heating_pumps_title = Resource(
        "Central heating pumps",
        "Pompy c.o."
    )

    val configurable_central_heating_pumps_description = Resource(
        "Central heating pumps coordinates the opening and closing of thermal actuators and the work of the pump itself.",
        "Pompy c.o. koordynują otwieranie i zamykanie siłowników termicznych oraz pracę samej pompy."
    )

    val configurable_circulation_pump_add = Resource(
        "Add circulation pump",
        "Dodaj pompę cyrkulacji"
    )

    val configurable_circulation_pump_edit = Resource(
        "Edit circulation pump",
        "Edytuj pompę cyrkulacji"
    )

    val configurable_circulation_pumps_title = Resource(
        "Circulation pumps",
        "Pompy cyrkulacyjne"
    )

    val configurable_circulation_pumps_description = Resource(
        "An automation of circulation pumps. The pump is pumping the water, as long as temperature reported by the thermometer is rising.",
        "Automatyka pompy cyrkulacyjnej. Pompa pompuje wodę tak długo, jak temperatura mierzona przez termometr rośnie."
    )
}