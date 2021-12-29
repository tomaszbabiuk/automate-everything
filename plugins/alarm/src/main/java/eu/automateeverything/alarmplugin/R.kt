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

package eu.automateeverything.alarmplugin

import eu.automateeverything.data.localization.Resource

object R {
    val field_combination_locks_hint = Resource(
        "Combination Locks",
        "Zamki szyfrowe"
    )

    val field_alarm_lines_hint = Resource(
        "Alarm lines",
        "Linie alarmowe"
    )

    var plugin_description = Resource(
        "Alarm devices",
        "Urządzenia do alarmu"
    )

    var plugin_name = Resource("Alarm", "Alarm")

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val field_contact_type_hint = Resource(
        "Contact type",
        "Rodzaj styku"
    )

    val field_delay_time_hint = Resource(
        "Delay time",
        "Czas opóźnienia"
    )

    val field_leaving_time_hint = Resource(
        "Leaving time",
        "Czas na wyjście"
    )

    val value_no = Resource(
        "Normally open",
        "Normalnie otwarte"
    )

    val value_nc = Resource(
        "Normally closed",
        "Normalnie zamknięte"
    )

    val configurable_alarmline_add = Resource(
        "Add an alarm line",
        "Dodaj linię alarmową"
    )

    val configurable_alarmline_edit = Resource(
        "Edit alarm line",
        "Edytuj linię alarmową"
    )

    val configurable_alarmlines_title = Resource(
        "Alarm lines",
        "Linie alarmowe"
    )

    val configurable_alarmlines_description = Resource(
        "Alarm line models the alarm circuit between the magnetic/movement/other detectors are and the alarm central unit.",
        "Linia alarmowa modeluje obwód alarmowy pomiędzy czujnikami (ruchu, otwarcia lub innymi), a cantralną jednostną alarmową."
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )

    val state_armed = Resource(
        "Armed",
        "Uzbrojony"
    )

    val state_disarmed = Resource(
        "Disarmed",
        "Rozbrojony"
    )

    val state_watching = Resource(
        "Watching",
        "Czuwanie"
    )

    val state_prealarm = Resource(
        "Prealarm",
        "Prealarm"
    )

    val state_alarm = Resource(
        "Alarm",
        "Alarm"
    )

    val state_leaving = Resource(
        "Leaving",
        "Opuszczanie obiektu"
    )

    val action_arm = Resource(
        "Arm",
        "Uzbrój"
    )

    val action_disarm = Resource(
        "Disarm",
        "Rozbrój"
    )

    val action_count = Resource(
        "Count and arm",
        "Odliczaj i uzbrój"
    )

    val field_status_port_hint = Resource(
        "Status port",
        "Port statusu"
    )

    val field_arming_port_hint = Resource(
        "Arming port",
        "Port uzbrajania"
    )

    val configurable_combinationlock_add = Resource(
        "Add a combination lock",
        "Dodaj szyfrator alarmowy"
    )

    val configurable_combinationlock_edit = Resource(
        "Edit combination lock",
        "Edytuj szyfrator alarmowy"
    )

    val configurable_combintationlocks_title = Resource(
        "Combination locks",
        "Szyfratory alarmowe"
    )

    val configurable_combinationlocks_description = Resource(
        "Add/edit combination locks. The combination lock is a type of locking device in which a sequence of numbers, is used to arm/disarm the alarm zone.",
        "Dodawanie/edycja szyfratorów alarmowych. Szyfratory są urządzeniami, które pozwalają na uzbrajanie/rozbrajanie strefy alarmowej za pomocą sekwencji kodu."
    )

    val configurable_alarmzone_add = Resource(
        "Add an alarm zone",
        "Dodaj strefę alarmową"
    )

    val configurable_alarmzone_edit = Resource(
        "Edit alarm zone",
        "Edytuj strefę alarmową"
    )

    val configurable_alarmzone_title = Resource(
        "Alarm zones",
        "Strefy alarmowe"
    )

    val configurable_alarmzone_description = Resource(
        "Alarm zone controls the group of alarm lines. It fires the alarm when armed and one of the alarm lines is breached.",
        "Strefa alarmowa kontroluje grupę lini alarmowych. Uruchamia alarm, gdy jest ubzrojona i jeden z czujników zostaje naruszony."
    )

    val configurable_alarmdevices_title = Resource(
        "Alarm devices",
        "Urządzenia alarmowe"
    )

    val configurable_alarmdevices_description = Resource(
        "Alarm lines, combination locks, alarm zones, etc...",
        "Linie alarmowe, szyfratory, strefy alarmowe itp..."
    )
}