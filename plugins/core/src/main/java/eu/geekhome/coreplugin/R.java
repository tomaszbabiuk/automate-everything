package eu.geekhome.coreplugin;

import com.geekhome.common.localization.Resource;

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

    public static Resource configurable_device_add = new Resource(
            "Add device",
            "Dodaj urządzenie");

    public static Resource configurable_device_title = new Resource(
            "Devices",
            "Urządzenia");

    public static Resource configurable_device_description = new Resource(
            "Add/remove devices (sensors, relays, switches, etc.)",
            "Dodawaj/usuwaj urządzenia (czujniki, przekaźniki, włączniki, itp.)");

    public static Resource configurable_scene_add = new Resource(
            "Add scene",
            "Dodaj scenę");

    public static Resource configurable_scene_title = new Resource(
            "Scenes",
            "Sceny");

    public static Resource configurable_scene_description = new Resource(
            "Scene is a set of conditions that defines specyfic behavior",
            "Sceny to zbiór warunków, które definiują określone zachowanie"
    );

    public static Resource configurable_floor_add = new Resource(
            "Add floor",
            "Dodaj piętro");

    public static Resource configurable_floor_title = new Resource(
            "House plan",
            "Plan domu");

    public static Resource configurable_floor_description = new Resource(
            "Group devices by location (in rooms, at floors)",
            "Grupuj urządzenia wg ich położenia (w pokojach, na piętrach)");

    public static Resource configurable_room_add = new Resource(
            "Add room",
            "Dodaj pokój");

    public static Resource configurable_room_title = new Resource(
            "Room",
            "Pokój");

    public static Resource configurable_room_description = new Resource(
            "Rooms are used to group devices in one location",
            "Pokoje są używane do gromadzenia urządzeń w jednej lokalizacji");


}
