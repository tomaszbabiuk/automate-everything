@file:Suppress("FunctionName")

package eu.automateeverything.domain

import eu.automateeverything.data.localization.Resource

object R {
    var category_this_device = Resource(
        "This device",
        "To urządzenie"
    )

    val category_triggers_conditions = Resource(
        "Conditions",
        "Warunki"
    )

    val category_triggers = Resource(
        "Triggers",
        "Wyzwalacze"
    )

    val category_logic = Resource(
        "Logic",
        "Logika"
    )

    val category_temperature = Resource(
        "Temperature",
        "Temperatura"
    )

    val category_state = Resource(
        "State",
        "Stan"
    )

    val category_wattage = Resource(
        "Wattage",
        "Moc"
    )

    val category_humidity = Resource(
        "Humidity",
        "Wilgotność"
    )

    val block_label_delay = Resource(
        "Wait for %1 seconds %2 and than: %3",
        "Poczekaj %1 sekund %2 potem: %4"
    )

    val block_label_if_than_else = Resource(
        "If %1 than %2 else %3",
        "Jeżeli %1 to %2 inaczej %3"
    )

    val block_label_and = Resource(
        "%1 and %2 %3",
        "%1 i %2 %3"
    )

    val block_label_or = Resource(
        "%1 or %2 %3",
        "%1 lub %2 %3"
    )

    val block_label_not = Resource(
        "not %1 %2",
        "nie %1 %2"
    )

    val block_label_repeat = Resource(
        "Repeat every %1",
        "Powtarzaj co %1"
    )

    val block_label_changes_state = Resource(
        "%1 changes state to %2",
        "%1 zmienia stan na %2"
    )

    val block_label_in_state = Resource(
        "%1 is in state %2",
        "%1 is in state %2"
    )

    val second = Resource(
        "second",
        "sekundę"
    )

    val seconds15 = Resource(
        "15 seconds",
        "15 sekund"
    )

    val seconds30 = Resource(
        "30 seconds",
        "30 sekund"
    )

    val minute = Resource(
        "minute",
        "minutę"
    )

    val minutes2 = Resource(
        "2 minutes",
        "2 minuty"
    )

    val minutes5 = Resource(
        "5 minutes",
        "5 minut"
    )

    val minutes10 = Resource(
        "10 minutes",
        "10 minut"
    )

    val minutes30 = Resource(
        "30 minutes",
        "30 minut"
    )

    val hour = Resource(
        "hour",
        "godzinę"
    )

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

    val state_unknown = Resource(
        "Unknown",
        "Nieznany"
    )
}