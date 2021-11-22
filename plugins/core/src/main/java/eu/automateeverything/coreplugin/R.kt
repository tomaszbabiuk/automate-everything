package eu.automateeverything.coreplugin

import eu.automateeverything.data.localization.Resource

object R {
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

    val field_readonly_hint = Resource(
        "Read only",
        "Tylko do odczytu"
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

    val field_preset1_hint = Resource(
        "Preset 1 (% of power)",
        "Ustawienie 1 (% mocy)"
    )

    val field_preset2_hint = Resource(
        "Preset 2 (% of power)",
        "Ustawienie 2 (% mocy)"
    )

    val field_preset3_hint = Resource(
        "Preset 3 (% of power)",
        "Ustawienie 3 (% mocy)"
    )

    val field_preset4_hint = Resource(
        "Preset 4 (% of power)",
        "Ustawienie 4 (% mocy)"
    )

    val field_longitude_hint = Resource(
        "Longitude",
        "Długość geograficzna"
    )

    val field_latitude_hint = Resource(
        "Latitude",
        "Szerekość geograficzna"
    )

    val configurable_meters_title = Resource(
        "Meters",
        "Urządzenia pomiarowe"
    )

    val configurable_meters_description = Resource(
        "Add/remove any type of meters (thermometers, wattmeters, etc.)",
        "Dodawaj/usuwaj urządzenia pomiarowe (termometry, watomierze, itp.)"
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

    val configurable_powerdevice_add = Resource(
        "Add power regulated device",
        "Dodaj urządzenie z regulacją mocy"
    )

    val configurable_powerdevice_edit = Resource(
        "Edit power regulated device",
        "Edytuj urządzenie z regulacją mocy"
    )

    val configurable_powerdevice_title = Resource(
        "Power regulated devices",
        "Urządzenia z regulacją mocy"
    )

    val configurable_powerdevices_description = Resource(
        "A devices which power can be regulated from 0-100%",
        "Urządzenie, których moc może być regulowana w zakresie od 0-100%"
    )

    val configurable_twilightcondition_add = Resource(
        "Add twilight condition",
        "Dodaj warunek zmierzchowy"
    )

    val configurable_twilightcondition_edit = Resource(
        "Edit twilight condition",
        "Edytuj warunek zmierzchowy"
    )

    val configurable_twilightcondition_title = Resource(
        "Twilight conditions",
        "Warunki zmierzchowe"
    )

    val configurable_twilightcondition_description = Resource(
        "Twilight condition can calculate the time of sunset and sunrise based on location data",
        "Warunek zmierzchowy potrafi obliczyć godzinę wschodu i zachodu słońca na podstawie podanej lokalizacji"
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

    val state_preset1 = Resource(
        "Preset 1",
        "Ustawienie 1"
    )

    val state_preset2 = Resource(
        "Preset 2",
        "Ustawienie 2"
    )

    val state_preset3 = Resource(
        "Preset 3",
        "Ustawienie 3"
    )

    val state_preset4 = Resource(
        "Preset 4",
        "Ustawienie 4"
    )

    val state_manual = Resource(
        "Manual",
        "Manual"
    )
}