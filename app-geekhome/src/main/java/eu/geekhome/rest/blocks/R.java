package eu.geekhome.rest.blocks;

import eu.geekhome.services.localization.Resource;
import org.jetbrains.annotations.NotNull;

public class R {
    public static Resource category_name_this_device = new Resource(
                "This device",
                "To urządzenie");

    public static final Resource category_name_conditions = new Resource(
            "Conditions",
            "Warunki");

    public static final Resource category_name_triggers = new Resource(
            "Triggers",
            "Wyzwalacze");

    public static final Resource category_name_logic = new Resource(
            "Logic",
            "Logika");

    public static final Resource block_label_if_than_else = new Resource(
            "If %1 than %2 else %3",
            "Jeżeli %1 to %2 inaczej %3");

    public static final Resource block_label_and = new Resource(
            "%1 and %2 %3",
            "%1 i %2 %3"
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
