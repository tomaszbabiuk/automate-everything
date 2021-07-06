package eu.geekhome.domain

import eu.geekhome.domain.localization.Resource

object R {
    var field_name_hint = Resource(
        "Name",
        "Nazwa"
    )
    var field_description_hint = Resource(
        "Description",
        "Opis"
    )
    var error_automation = Resource(
        "Automation error",
        "Błąd automatyki"
    )

    fun error_port_not_found(portId: String): Resource {
        return Resource(
            "Port $portId not found",
            "Nie odnaleziono portu $portId"
        )
    }

    var error_initialization = Resource(
        "Initialization error",
        "Błąd inicjalizacji"
    )
    var error_other_device_failure = Resource(
        "Automation of this device uses another device that's not available",
        "Automatyka tego urządzenia używa innego urządzenia, które jest niedostępne"
    )
    var validator_required_field = Resource(
        "This field is required",
        "To pole jest wymagane"
    )
    var validator_invalid_field = Resource(
        "Invalid field",
        "To pole jest nieprawidłowe"
    )
    var validator_invalid_ip_address = Resource(
        "Invalid ip address",
        "Nieprawidłowy adres IP"
    )
}