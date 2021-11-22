package eu.automateeverything.centralheatingplugin

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
        "Edytuj sterewnik temperatury"
    )

    val configurable_temperature_controller_add = Resource(
        "Add temperature controller",
        "Dodaj sterownik temperatury"
    )


    var plugin_description = Resource(
        "Central Heating devices",
        "Urządzenia do centralnego ogrzewania"
    )

    var plugin_name = Resource("Central heating", "Centralne ogrzewanie")
}