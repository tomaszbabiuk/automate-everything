package eu.geekhome.services;

import eu.geekhome.services.localization.Resource;

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
}
