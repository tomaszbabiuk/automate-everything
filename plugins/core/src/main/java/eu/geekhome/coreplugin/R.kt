package eu.geekhome.coreplugin

import eu.geekhome.services.localization.Resource

object R {
    val field_ip_from = Resource(
        "Starting IPv4 address, the discovery will start from this address",
        "Adres startowy IPv4 od którego zacznie się wykrywanie urządzeń"
    )

    val field_ip_to = Resource(
        "Ending IPv4 address, the discovery will finish on this address",
        "Adres końcowy IPv4 na którym zakończy się wykrywanie urządzeń"
    )

    val settings_lan_discovery_title = Resource(
      "LAN discovery",
      "Wykrywanie urządzeń w sieci LAN"
    )

    val settings_lan_discovery_description = Resource(
      "Local IP address range that will be checked for being a smart device during discovery process",
      "Zakres adresów IP w sieci lokalnej, które będą odpytywane w czasie procesu wyszukiwania inteligentnych urządzeń"
    )

    val plugin_description = Resource(
        "Core definitions of devices and conditions",
        "Główne definicje urządzeń i warunków"
    )

    val plugin_name = Resource(
        "Core",
        "Core"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val field_longitude_hint = Resource(
        "Longitude",
        "Długość geograficzna"
    )

    val field_latitude_hint = Resource(
        "Latitude",
        "Szerekość geograficzna"
    )

    val configurable_device_title = Resource(
        "Devices",
        "Urządzenia"
    )

    val configurable_device_description = Resource(
        "Add/remove devices (sensors, relays, switches, etc.)",
        "Dodawaj/usuwaj urządzenia (czujniki, przekaźniki, włączniki, itp.)"
    )

    val configurable_conditions_title = Resource(
        "Conditions",
        "Warunki"
    )

    val configurable_conditions_description = Resource(
        "Add/remove conditions",
        "Dodawaj/usuwaj warunki"
    )

    val configurable_meters_title = Resource(
        "Meters",
        "Urządzenia pomiarowe"
    )

    val configurable_meters_description = Resource(
        "Add/remove any type of meters (thermometers, wattmeters, etc.)",
        "Dodawaj/usuwaj urządzenia pomiarowe (termometry, watomierze, itp.)"
    )

    val configurable_scene_add = Resource(
        "Add scene",
        "Dodaj scenę"
    )

    val configurable_scene_edit = Resource(
        "Edit scene",
        "Edytuj scenę"
    )

    val configurable_scene_title = Resource(
        "Scenes",
        "Sceny"
    )

    val configurable_scene_description = Resource(
        "Scene is a set of conditions that define specyfic behavior",
        "Sceny to zbiór warunków, które definiują określone zachowanie"
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
        "Proste w autmatyzowaniu urządzenia typu: włącz/wyłącz"
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

    val configurable_thermometer_title = Resource(
        "Thermometers",
        "Termometry"
    )

    val configurable_thermometer_description = Resource(
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

    val state_on = Resource(
        "On",
        "Wł"
    )

    val state_off = Resource(
        "Off",
        "Wył"
    )
}