package eu.automateeverything.coreplugin

import eu.automateeverything.data.localization.Resource

object R {

    val configurable_temperature_controller_description = Resource(
        "Temperature controllers are used to control the temperature (eg. in a room). You can define a unique temperature setting for every scene they are bound to.",
        "Sterowniki temperatury pozwalają kontrolować temperaturę (np. w pomieszeniu). Możesz zdefiniować unikalną wartość nastawy dla każdej sceny, która jest powiązana z danym kontrolerem."
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

    val plugin_description = Resource(
        "Core definitions of devices and conditions",
        "Główne definicje urządzeń i warunków"
    )

    val plugin_name = Resource(
        "Core automation",
        "Automatyka podstawowa"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val field_automation_only_hint = Resource(
        "Automation control only",
        "Kontrola tylko przez automatykę"
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