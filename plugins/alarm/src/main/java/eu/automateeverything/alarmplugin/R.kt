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
        "Add alarm line",
        "Dodaj linię alarmową"
    )

    val configurable_alarmline_edit = Resource(
        "Edit alarm line",
        "Edytuj linię alarmową"
    )

    val configurable_alarmlines_title = Resource(
        "Alarm lines",
        "Alarm lines"
    )

    val configurable_alarmlines_description = Resource(
        "Alarm lines models the circuts between the magnetic/movement/other detectors are and the alarm central unit.",
        "Linie alarmowe modelują obwody pomiędzy czujnikami (ruchu, otwarcia lub innymi), a cantralną jednostną alarmową."
    )

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
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
}