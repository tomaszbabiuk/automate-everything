package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;

public class R {
    public static Resource plugin_description = new Resource(
                "Core definitions of devices and conditions",
                "Główne definicje urządzeń i warunków");

    public static Resource plugin_name = new Resource(
            "Core",
            "Core");

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

    public static Resource configurable_conditions_title = new Resource(
            "Conditions",
            "Warunki");

    public static Resource configurable_conditions_description = new Resource(
            "Add/remove conditions",
            "Dodawaj/usuwaj warunki");

    public static Resource configurable_meters_title = new Resource(
            "Meters",
            "Urządzenia pomiarowe");

    public static Resource configurable_meters_description = new Resource(
            "Add/remove any type of meters (thermometers, wattmeters, etc.)",
            "Dodawaj/usuwaj urządzenia pomiarowe (termometry, watomierze, itp.)");

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

    public static Resource configurable_hygrometer_add = new Resource(
            "Add hygrometer",
            "Dodaj higrometr");

    public static Resource configurable_hygrometer_edit = new Resource(
            "Edit hygrometer",
            "Edytuj higrometr");

    public static Resource configurable_hygrometer_title = new Resource(
            "Hygrometers",
            "Higrometry");

    public static Resource configurable_hygrometer_description = new Resource(
            "Humidity sensors",
            "Czujniki wilgotności");

    public static Resource configurable_wattmeter_add = new Resource(
            "Add wattmeter",
            "Dodaj watomierz");

    public static Resource configurable_wattmeter_edit = new Resource(
            "Edit wattmeter",
            "Edytuj watomierz");

    public static Resource configurable_wattmeter_title = new Resource(
            "Wattmeters",
            "Watomierze");

    public static Resource configurable_wattmeter_description = new Resource(
            "Wattmeter sensors",
            "Mierniki poboru prądu");

    public static Resource state_on = new Resource(
            "On",
            "Wł");

    public static Resource state_off = new Resource(
            "Off",
            "Wył");
}
