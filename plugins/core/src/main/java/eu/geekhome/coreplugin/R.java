package eu.geekhome.coreplugin;

import com.geekhome.common.localization.Resource;

public class R {
    public static Resource plugin_description = new Resource("CORE:plugin_description",
                "Core definitions of devices and conditions",
                "Główne definicje urządzeń i warunków");

    public static Resource plugin_name = new Resource("CORE:plugin_name",
            "Core",
            "Core");

    public static Resource field_name_hint = new Resource("CORE:field_name",
            "Name",
            "Nazwa");

    public static Resource field_description_hint = new Resource("CORE:field_description_hint",
            "Description",
            "Opis");

    public static Resource configurable_device_add = new Resource("CORE:configurable_device_add",
            "Add device",
            "Dodaj urządzenie");

    public static Resource configurable_device_title = new Resource("CORE:configurable_device_title",
            "Devices",
            "Urządzenia");

    public static Resource configurable_device_description = new Resource("CORE:configurable_device_description",
            "Devices are barebone of Home Automation. Every device is responsible for certain automation action like controlling the garage door, switching the lights, measuring the temperature and many more. Devices can be physical (connected to real ports) or virtual (that exists in automation interface only).",
            "Urządzenia to podstawowe elementy automatyki budynkowej. Każde urządzenie jest odpowiedzialne za przeprowadzanie akcji automatyzacji jak np. otwieranie bramy garażowej, włączanie światła, mierzenie temperatury itp. Urządzenie może być fizyczne (połączone do prawdziwych portów) lub wirtualne (takie, które istnieje tylko w interfejsie sterowania domem).");

    public static Resource configurable_scene_add = new Resource("CORE:configurable_scene_add",
            "Add scene",
            "Dodaj scenę");

    public static Resource configurable_scene_title = new Resource("CORE:configurable_scene_title",
            "Scenes",
            "Sceny");

    public static Resource configurable_group_add = new Resource("CORE:configurable_group_add",
            "Add group",
            "Dodaj grupę");

    public static Resource configurable_group_title = new Resource("CORE:configurable_group_title",
            "Group",
            "Grupa");

    public static Resource configurable_groups_title = new Resource("CORE:configurable_groups_title",
            "Groups",
            "Grupy");

    public static Resource configurable_floor_add = new Resource("CORE:configurable_floor_add",
            "Add floor",
            "Dodaj piętro");

    public static Resource configurable_floor_title = new Resource("CORE:configurable_floor_title",
            "House plan",
            "Plan domu");

    public static Resource configurable_floor_description = new Resource("CORE:configurable_floor_description",
            "House plan allows you to better organize your devices. Every house consists of at least one Floor and multiple Rooms. Having house plan created, devices can be grouped together across rooms in which they are installed.",
            "Plan domu pozwala na lepszą organizację urządzeń. Każdy dom składa się z conajmniej jednego piętra i kilku pokoi. Dzięki planowi, wszystkie urządzenia mogą być pogrupowane wg pomieszczeń w których są zainstalowane.");

    public static Resource configurable_room_add = new Resource("CORE:configurable_room_add",
            "Add room",
            "Dodaj pokój");

    public static Resource configurable_room_title = new Resource("CORE:configurable_room_title",
            "Room",
            "Pokój");

    public static Resource configurable_room_description = new Resource("CORE:configurable_room_description",
            "Rooms are used to organize/group devices in one physical space. Rooms can only be added to Floors",
            "Pokoje są używane do organizowania/grupowania urządzeń w jednej fizycznej przestrzeni. Pokoje mogą być tylko dodawane do Pięter.");

}
