package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;

public class R {
    public static Resource plugin_description = new Resource(
                "Core definitions of devices and conditions",
                "Główne definicje urządzeń i warunków");

    public static Resource plugin_name = new Resource(
            "Core",
            "Core");

    public static Resource field_name_hint = new Resource(
            "Name",
            "Nazwa");

    public static Resource field_description_hint = new Resource(
            "Description",
            "Opis");

    public static Resource field_name_port = new Resource(
            "Port",
            "Port");

    public static Resource configurable_device_title = new Resource(
            "Devices",
            "Urządzenia");
    public static Resource configurable_device_description = new Resource(
            "Add/remove devices (sensors, relays, switches, etc.)",
            "Dodawaj/usuwaj urządzenia (czujniki, przekaźniki, włączniki, itp.)");

    public static Resource configurable_scene_add = new Resource(
            "Add scene",
            "Dodaj scenę");

    public static Resource configurable_scene_edit = new Resource(
            "Edit scene",
            "Edytuj scenę");

    public static Resource configurable_scene_title = new Resource(
            "Scenes",
            "Sceny");

    public static Resource configurable_scene_description = new Resource(
            "Scene is a set of conditions that define specyfic behavior",
            "Sceny to zbiór warunków, które definiują określone zachowanie");


    public static Resource configurable_onoffdevice_add = new Resource(
            "Add on/off device",
            "Dodaj urządzenie wł/wył");

    public static Resource configurable_onoffdevice_edit = new Resource(
            "Edit on/off device",
            "Edytuj urządzenie wł/wył");

    public static Resource configurable_onoffdevice_title = new Resource(
            "On/Off devices",
            "Urządzenia wł/wył");

    public static Resource configurable_onoffdevices_description = new Resource(
            "A very simple to automate on/off devices",
            "Proste w autmatyzowaniu urządzenia typu: włącz/wyłącz");

    public static Resource configurable_thermometer_add = new Resource(
            "Add thermometer",
            "Dodaj termometr");

    public static Resource configurable_thermometer_edit = new Resource(
            "Edit thermometer",
            "Edytuj termometr");

    public static Resource configurable_thermometer_title = new Resource(
            "Thermometers",
            "Termometry");

    public static Resource configurable_thermometer_description = new Resource(
            "Temperature sensors",
            "Czujniki temperatury");

}
