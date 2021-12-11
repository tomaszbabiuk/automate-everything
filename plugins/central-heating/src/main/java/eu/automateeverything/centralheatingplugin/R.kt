package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.localization.Resource

object R {
    val field_temperature_controller_hint = Resource(
        "Temperature controller",
        "Sterownik temperatury"
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

    val field_thermometer_hint = Resource(
        "Ambient thermometer",
        "Termometr temperatury otoczenia"
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
        "Dodatj obieg grzejnikowy"
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
}