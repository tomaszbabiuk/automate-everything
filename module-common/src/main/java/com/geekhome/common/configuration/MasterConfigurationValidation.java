package com.geekhome.common.configuration;

public class MasterConfigurationValidation {
    public static boolean canAddChangeStateCommands(MasterConfiguration config) {
        return isAnyNonReadonlyMultistateDeviceDefined(config);
    }

    public static boolean canAddDevices(MasterConfiguration config) {
        return isAnyRoomDefined(config);
    }

    public static boolean canAddGroupConditions(MasterConfiguration masterConfiguration) {
        return isAnyConditionDefined(masterConfiguration);
    }

    public static boolean canAddMultistateConditions(MasterConfiguration masterConfiguration) {
        return isAnyMultistateDeviceDefined(masterConfiguration);
    }

    public static boolean canAddValueConditions(MasterConfiguration masterConfiguration) {
        return isAnyValueDeviceDefined(masterConfiguration);
    }

    public static boolean canAddGeofenceConditions(MasterConfiguration masterConfiguration) {
        return masterConfiguration.getGeofences().size() > 0;
    }

    public static boolean canAddDeltaConditions(MasterConfiguration masterConfiguration) {
        return isAnyValueDeviceDefined(masterConfiguration);
    }

    private static boolean isAnyMultistateDeviceDefined(MasterConfiguration masterConfiguration) {
        for (CollectorCollection collection : masterConfiguration.getDevicesCollectors()) {
            for (Object item : collection.values())
                if (item instanceof IDevice) {
                    IDevice device = (IDevice) item;
                    if (device instanceof MultistateDevice) {
                        return true;
                    }
                }
        }

        return false;
    }

    private static boolean isAnyValueDeviceDefined(MasterConfiguration masterConfiguration) {
        for (CollectorCollection collection : masterConfiguration.getDevicesCollectors()) {
            for (Object item : collection.values())
                if (item instanceof IDevice) {
                    IDevice device = (IDevice) item;
                    if (device instanceof IValueDevice) {
                        return true;
                    }
                }
        }

        return false;
    }

    private static boolean isAnyNonReadonlyMultistateDeviceDefined(MasterConfiguration masterConfiguration) {
        for (CollectorCollection collection : masterConfiguration.getDevicesCollectors()) {
            for (Object item : collection.values())
                if (item instanceof IDevice) {
                    IDevice device = (IDevice) item;
                    if (device instanceof  MultistateDevice && ((MultistateDevice) device).hasNonReadonlyStates()) {
                        return true;
                    }
                }
        }

        return false;
    }

    private static boolean isAnyConditionDefined(MasterConfiguration masterConfiguration) {
        for (CollectorCollection collection : masterConfiguration.getConditionsCollectors()) {
            if (collection.values().size() > 0) {
                return true;
            }
        }

        return false;
    }

    protected static boolean isAnyRoomDefined(MasterConfiguration coreConfiguration) {
        for (Floor floor : coreConfiguration.getFloors().values()) {
            if (floor.getRooms().size() > 0) {
                return true;
            }
        }

        return false;
    }
}
