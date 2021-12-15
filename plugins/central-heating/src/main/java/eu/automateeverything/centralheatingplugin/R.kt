package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.localization.Resource

object R {

    @Suppress("FunctionName")
    fun note_opening_level(value: Int) = Resource(
        "Valve opening: $value",
        "Otwarcie zaworu: $value"
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

    val field_circuits_hint = Resource(
        "Circuits",
        "Obiegi"
    )

    val field_minimum_pump_working_time_hint = Resource(
        "Minumum pump working time",
        "Minimalny czas pracy pompy"
    )

    val field_pump_port_hint = Resource(
        "Pump port",
        "Port pompy c.o."
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

    val configurable_radiator_circuit_add = Resource(
        "Add radiator circuit",
        "Dodaj obieg grzejnikowy"
    )

    val configurable_radiator_circuit_edit = Resource(
        "Edit a radiator circuit",
        "Edytuj obieg grzejnikowy"
    )

    val configurable_radiator_circuits_title = Resource(
        "Radiator circuits",
        "Obiegi grzejnikowe"
    )

    val configurable_radiator_circuits_description = Resource(
        "Defines an radiator circuit opened by thermal or mechanical actuator.",
        "Definiuje obieg grzejnikowy otwierany przez siłownik termiczny lub mechaniczny."
    )

    val configurable_heating_manifold_add = Resource(
        "Add heating manifold",
        "Dodaj rozdzielacz c.o."
    )

    val configurable_heating_manifold_edit = Resource(
        "Edit a heating manifold",
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
}