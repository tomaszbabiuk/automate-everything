package eu.geekhome.automation;

import eu.geekhome.services.localization.Resource;

public class R {
    public static Resource category_this_device = new Resource(
                "This device",
                "To urządzenie");

    public static final Resource category_triggers_conditions = new Resource(
            "Conditions",
            "Warunki");

    public static final Resource category_triggers = new Resource(
            "Triggers",
            "Wyzwalacze");

    public static final Resource category_logic = new Resource(
            "Logic",
            "Logika");

    public static final Resource category_temperature = new Resource(
            "Temperature",
            "Temperatura");

    public static final Resource category_wattage = new Resource(
            "Wattage",
            "Moc");

    public static final Resource category_humidity = new Resource(
            "Humidity",
            "Wilgotność");

    public static final Resource block_label_if_than_else = new Resource(
            "If %1 than %2 else %3",
            "Jeżeli %1 to %2 inaczej %3");

    public static final Resource block_label_and = new Resource(
            "%1 and %2 %3",
            "%1 i %2 %3"
    );

    public static final Resource block_label_or = new Resource(
            "%1 or %2 %3",
            "%1 lub %2 %3"
    );

    public static final Resource block_label_not = new Resource(
            "not %1 %2",
            "nie %1 %2"
    );

    public static final Resource block_label_repeat = new Resource(
            "Repeat every %1",
            "Powtarzaj co %1");

    public static final Resource second = new Resource(
            "second",
            "sekundę");

    public static final Resource minute = new Resource(
            "minute",
            "minutę");

    public static final Resource hour = new Resource(
            "hour",
            "godzinę");
}
