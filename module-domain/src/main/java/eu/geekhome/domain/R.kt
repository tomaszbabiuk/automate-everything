@file:Suppress("FunctionName")

package eu.geekhome.domain

import eu.geekhome.domain.localization.Resource

object R {
    val field_name_hint = Resource(
        "Name",
        "Nazwa"
    )

    val field_description_hint = Resource(
        "Description",
        "Opis"
    )

    val error_automation = Resource(
        "Automation error",
        "Błąd automatyki"
    )

    fun error_port_not_found(portId: String): Resource {
        return Resource(
            "Port $portId not found or disconnected",
            "Nie odnaleziono portu $portId lub port został rozłączony"
        )
    }

    val error_initialization = Resource(
        "Initialization error",
        "Błąd inicjalizacji"
    )

    val error_other_device_failure = Resource(
        "Automation of this device uses another device that's not available",
        "Automatyka tego urządzenia używa innego urządzenia, które jest niedostępne"
    )

    fun error_other_device_failure(unknownName: String) = Resource(
        "Automation of this device uses another device ($unknownName) that's not available",
        "Automatyka tego urządzenia używa innego urządzenia ($unknownName), które jest niedostępne"
    )
    
    val error_unknown_device_name = Resource(
        "no name",
        "bez nazwy"
    )
    
    val validator_required_field = Resource(
        "This field is required",
        "To pole jest wymagane"
    )

    val validator_invalid_field = Resource(
        "Invalid field",
        "To pole jest nieprawidłowe"
    )

    val validator_invalid_ip_address = Resource(
        "Invalid ip address",
        "Nieprawidłowy adres IP"
    )
}