package eu.geekhome.domain;

import eu.geekhome.domain.localization.Resource;

public class R {
    public static Resource field_name_hint = new Resource(
            "Name",
            "Nazwa");

    public static Resource field_description_hint = new Resource(
            "Description",
            "Opis");

    public static Resource error_automation = new Resource(
            "Automation error",
            "Błąd automatyki");

    public static Resource error_initialization = new Resource(
            "Initialization error",
            "Błąd inicjalizacji");

    public static Resource validator_required_field = new Resource(
            "This field is required",
            "To pole jest wymagane"
    );

    public static Resource validator_invalid_field = new Resource(
            "Invalid field",
            "To pole jest nieprawidłowe"
    );

    public static Resource validator_invalid_ip_address = new Resource(
            "Invalid ip address",
            "Nieprawidłowy adres IP"
    );
}