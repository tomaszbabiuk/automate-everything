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

package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.data.localization.Resource

object R {
    val configurable_luminosity_controller_description = Resource(
        "Luminosity controllers are used to control the setting of luminance (eg. in a room).",
        "Sterowniki jasności pozwalają regulować nastawę natężenia oświetlenia (np. w pomieszeniu)."
    )

    val configurable_luminosity_controller_title = Resource(
        "Luminosity controllers",
        "Sterowniki jasności"
    )

    val configurable_luminosity_controller_edit = Resource(
        "Edit luminosity controller",
        "Edytuj sterownik jasności"
    )

    val configurable_luminosity_controller_add = Resource(
        "Add luminosity controller",
        "Dodaj sterownik oświetlenia"
    )

    val configurable_temperature_controller_description = Resource(
        "Temperature controllers are used to control the setting of temperature (eg. in a room).",
        "Sterowniki temperatury pozwalają regulować nastawę temperatury (np. w pomieszeniu)."
    )

    val configurable_temperature_controller_title = Resource(
        "Temperature controllers",
        "Sterowniki temperatury"
    )

    val configurable_temperature_controller_edit = Resource(
        "Edit temperature controller",
        "Edytuj sterownik temperatury"
    )

    val configurable_temperature_controller_add = Resource(
        "Add temperature controller",
        "Dodaj sterownik temperatury"
    )

    val configurable_humidity_controller_description = Resource(
        "Humidity controllers are used to control the setting of humidity (eg. in a room).",
        "Sterowniki wilgotności pozwalają regulować nastawę wilgotności (np. w pomieszeniu). "
    )

    val configurable_humidity_controller_title = Resource(
        "Humidity controllers",
        "Sterowniki wilgotności"
    )

    val configurable_humidity_controller_edit = Resource(
        "Edit humidity controller",
        "Edytuj sterownik wilgotności"
    )

    val configurable_humidity_controller_add = Resource(
        "Add humidity controller",
        "Dodaj sterownik wilgotności"
    )

    val field_min_temp_hint = Resource(
        "Minimum temperature",
        "Temperatura minimalna"
    )

    val field_max_temp_hint = Resource(
        "Maximum temperature",
        "Temperatura maksymalna"
    )

    val field_default_temp_hint = Resource(
        "Default temperature",
        "Temperatura domyślna"
    )

    val field_min_lum_hint = Resource(
        "Minimum luminosity [lux]",
        "Jasność minimalna [lux]"
    )

    val field_max_lum_hint = Resource(
        "Maximum luminosity [lux]",
        "Jasność maksymalna [lux]"
    )

    val field_default_lum_hint = Resource(
        "Default luminosity [lux]",
        "Jasność domyślna [lux]"
    )

    val field_automation_only_hint = Resource(
        "Automation control only",
        "Kontrola tylko przez automatykę"
    )

    val field_min_hum_hint = Resource(
        "Minimum humidity",
        "Wilgotność minimalna"
    )

    val field_max_hum_hint = Resource(
        "Maximum humidity",
        "Wilgotności maksymalna"
    )

    val field_default_hum_hint = Resource(
        "Default humidity",
        "Wilgotności domyślna"
    )

    val plugin_description = Resource(
        "Core sensor devices: Thermometers, Hygrometers, Wattmeters, etc.",
        "Podstawowe czujniki: temperatury, wilgotności, zużycie prądu, itp."
    )

    val plugin_name = Resource(
        "Core Sensors",
        "Podstawowe czujniki/sensory"
    )

    val configurable_sensors_title = Resource(
        "Sensors",
        "Urządzenia pomiarowe"
    )

    val configurable_sensors_description = Resource(
        "Add/remove any type of sensors (thermometers, wattmeters, etc.)",
        "Dodawaj/usuwaj urządzenia pomiarowe (termometry, watomierze, itp.)"
    )

    val configurable_controllers_title = Resource(
        "Controllers",
        "Kontrolery"
    )

    val configurable_controllers_description = Resource(
        "Add/remove any type of controllers (temperature controlllers, humidity controllers, etc.)",
        "Dodawaj/usuwaj kontrolery (temperatury, wilgotności, itp.)"
    )

    val configurable_thermometer_add = Resource(
        "Add thermometer",
        "Dodaj termometr"
    )

    val configurable_thermometer_edit = Resource(
        "Edit thermometer",
        "Edytuj termometr"
    )

    val configurable_thermometers_title = Resource(
        "Thermometers",
        "Termometry"
    )

    val configurable_thermometers_description = Resource(
        "Temperature sensors",
        "Czujniki temperatury"
    )

    val configurable_hygrometer_add = Resource(
        "Add hygrometer",
        "Dodaj higrometr"
    )

    val configurable_hygrometer_edit = Resource(
        "Edit hygrometer",
        "Edytuj higrometr"
    )

    val configurable_hygrometer_title = Resource(
        "Hygrometers",
        "Higrometry"
    )

    val configurable_hygrometer_description = Resource(
        "Humidity sensors",
        "Czujniki wilgotności"
    )

    val configurable_wattmeter_add = Resource(
        "Add wattmeter",
        "Dodaj watomierz"
    )

    val configurable_wattmeter_edit = Resource(
        "Edit wattmeter",
        "Edytuj watomierz"
    )

    val configurable_wattmeter_title = Resource(
        "Wattmeters",
        "Watomierze"
    )

    val configurable_wattmeter_description = Resource(
        "Wattmeter sensors",
        "Mierniki poboru prądu"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val configurable_switch_add = Resource(
        "Add switch",
        "Dodaj przełącznik"
    )

    val configurable_switch_edit = Resource(
        "Edit switch",
        "Edytuj przełącznik"
    )

    val configurable_switches_title = Resource(
        "Switches",
        "Przełączniki"
    )

    val configurable_switches_description = Resource(
        "Switches/buttons",
        "Przełączniki i włączniki"
    )
}