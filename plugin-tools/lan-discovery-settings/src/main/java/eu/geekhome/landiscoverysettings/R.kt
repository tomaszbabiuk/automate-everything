package eu.geekhome.landiscoverysettings

import eu.geekhome.domain.localization.Resource

object R {
    val field_ip_from = Resource(
        "Starting IPv4 address",
        "Adres startowy IPv4"
    )

    val field_ip_to = Resource(
        "Ending IPv4 address",
        "Adres końcowy IPv4"
    )

    val settings_lan_discovery_title = Resource(
      "LAN discovery",
      "Wykrywanie urządzeń w sieci LAN"
    )

    val settings_lan_discovery_description = Resource(
      "Local IP address range that will be checked for being a smart device during discovery process",
      "Zakres adresów IP w sieci lokalnej, które będą odpytywane w czasie procesu wyszukiwania inteligentnych urządzeń"
    )
}