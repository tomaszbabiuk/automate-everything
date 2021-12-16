package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.localization.Resource

object R {

    @Suppress("FunctionName")
    fun note_opening_level(value: Int) = Resource(
        "Valve opening: $value",
        "Otwarcie zaworu: $value"
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

    val state_regulation = Resource(
        "Regulation",
        "Regulacja"
    )

    val field_thermal_actuators_hint = Resource(
        "Thermal actuators",
        "Siłowniki termiczne"
    )

    val field_minimum_pump_working_time_hint = Resource(
        "Minimum pump working time",
        "Minimalny czas pracy pompy"
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
        "Devices that control central heating (radiators, heating manifolds, etc.)",
        "Urządzenia do sterowania centralnym ogrzewaniem (grzejniki, rozdzielacze c.o. itp.)"
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

    val configurable_heating_manifold_add = Resource(
        "Add heating manifold",
        "Dodaj rozdzielacz c.o."
    )

    val configurable_heating_manifold_edit = Resource(
        "Edit heating manifold",
        "Edytuj rozdzialacz c.o."
    )

    val configurable_heating_manifolds_title = Resource(
        "Heating manifolds",
        "Rozdzialacze c.o."
    )

    val configurable_heating_manifolds_description = Resource(
        "Heating manifolds control thermal actuators and a pump in the central heating system.",
        "Rozdzielacze c.o. kontrolują termo-siłowniki oraz pompę w systemie centralnego ogrzewania ."
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