package eu.automateeverything.alarmplugin

import eu.automateeverything.data.localization.Resource

object R {
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
        "Edit an alarm line",
        "Edytuj linię alarmową"
    )

    val configurable_alarmlines_title = Resource(
        "Alarm lines",
        "Alarm lines"
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
        "Edit a combination lock",
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
}