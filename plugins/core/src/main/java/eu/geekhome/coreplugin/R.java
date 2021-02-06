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

    public static Resource field_port_hint = new Resource(
            "Port",
            "Port");

    public static Resource field_longitude_hint = new Resource(
            "Longitude",
            "Długość geograficzna");

    public static Resource field_latitude_hint = new Resource(
            "Latitude",
            "Szerekość geograficzna");

    public static Resource configurable_device_title = new Resource(
            "Devices",
            "Urządzenia");

    public static Resource configurable_device_description = new Resource(
            "Add/remove devices (sensors, relays, switches, etc.)",
            "Dodawaj/usuwaj urządzenia (czujniki, przekaźniki, włączniki, itp.)");

    public static Resource configurable_condition_title = new Resource(
            "Conditions",
            "Warunki");

    public static Resource configurable_condition_description = new Resource(
            "Add/remove conditions",
            "Dodawaj/usuwaj warunki");

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

    public static Resource configurable_twilightcondition_add = new Resource(
            "Add twilight condition",
            "Dodaj warunek zmierzchowy");

    public static Resource configurable_twilightcondition_edit = new Resource(
            "Edit twilight condition",
            "Edytuj warunek zmierzchowy");

    public static Resource configurable_twilightcondition_title = new Resource(
            "Twilight conditions",
            "Warunki zmierzchowe");

    public static Resource configurable_twilightcondition_description = new Resource(
            "Twilight condition can calculate the time of sunset and sunrise based on location data",
            "Warunek zmierzchowy potrafi obliczyć godzinę wschodu i zachodu słońca na podstawie podanej lokalizacji");

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

    public static Resource block_target_onoff = new Resource(
            "On/Off automation",
            "Automatyka Wł/Wył");
}
