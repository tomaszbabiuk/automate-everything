package com.geekhome.common.commands;

import com.geekhome.common.configuration.*;
import com.geekhome.common.INamedObject;
import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.State;
import com.geekhome.common.automation.ControlMode;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.automation.IDeviceAutomationUnit;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.configuration.ObjectNotFoundException;
import com.geekhome.synchronizationmodule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class IntegrationFactory {
    public static SynchronizedNamedObject create(INamedObject namedObject) {
        SynchronizedNamedObject result = new SynchronizedNamedObject();
        setDescriptiveName(namedObject, result);
        return result;
    }

    private static void setDescriptiveName(INamedObject namedObject, SynchronizedNamedObject result) {
        result.setName(namedObject.getName().getName());
        result.setDescription(namedObject.getName().getDescription());
        result.setUniqueId(namedObject.getName().getUniqueId());
    }

    public static SynchronizedCommandResult create(AutomationSettings automationSettings, ILocalizationProvider localizationProvider,
                                                   CollectorCollection<Mode> modes, CommandResult processedCommand) throws ObjectNotFoundException {
        SynchronizedCommandResult result = new SynchronizedCommandResult();
        result.setOutput(processedCommand.getOutput());
        result.setCodeRequired(processedCommand.isCodeRequired());
        if (processedCommand.getControlledDevice() != null) {
            result.setControlledDevice(IntegrationFactory.create(localizationProvider, modes, processedCommand.getControlledDevice()));
        }

        if (processedCommand.getAutomationUnits() != null) {
            Map<String, SynchronizedEvaluationResult> evaluationResults = new HashMap<>();
            for (IDeviceAutomationUnit unit : processedCommand.getAutomationUnits()) {
                SynchronizedEvaluationResult evaluationResult = IntegrationFactory.create(unit, automationSettings);
                evaluationResults.put(unit.getDevice().getName().getUniqueId(), evaluationResult);
            }
            result.setEvaluations(evaluationResults);
        }

        return result;
    }

    public static SynchronizedRoom create(Room room) {
        SynchronizedRoom result = new SynchronizedRoom();
        setDescriptiveName(room, result);
        result.setIconName(room.getIconName());
        return result;
    }

    public static SynchronizedGeofence create(Geofence geofence) {
        SynchronizedGeofence result = new SynchronizedGeofence();
        setDescriptiveName(geofence, result);
        result.setIconName(geofence.getIconName());
        result.setLongitude(geofence.getLongitude());
        result.setLatitude(geofence.getLatitude());
        result.setRadius(geofence.getRadius());
        return result;
    }

    public static SynchronizedFloor create(Floor floor) {
        SynchronizedFloor result = new SynchronizedFloor();
        setDescriptiveName(floor, result);
        result.setIconName(floor.getIconName());
        result.setRooms(new HashMap<>());
        for (Room room : floor.getRooms().values()) {
            result.getRooms().put(room.getName().getUniqueId(), IntegrationFactory.create(room));
        }

        return result;
    }

    public static SynchronizedSystemInfo create(SystemInfo systemInfo, MasterConfiguration masterConfiguration,
                                                 CollectorCollection<Alert> alerts) {
        SynchronizedSystemInfo result = new SynchronizedSystemInfo();
        result.setOperationMode(systemInfo.getOperationMode().toInt());
        result.setConfigVersion(masterConfiguration.getVersion());
        result.setSystemVersion("geekHOME/1.0");

        Map<String, SynchronizedAlert> synchronizedAlerts = new HashMap<>();
        for (Alert alert : alerts.values()) {
            synchronizedAlerts.put(alert.getName().getUniqueId(), IntegrationFactory.create(alert));
        }
        result.setAlerts(synchronizedAlerts);
        return result;
    }

    public static SynchronizedAlert create (Alert alert) {
        SynchronizedAlert result = new SynchronizedAlert();
        setDescriptiveName(alert, result);
        result.setRaisedOn(alert.getRaisedOn());
        result.setDevicesIds(alert.getDevicesIds());
        return result;
    }

    public static SynchronizedDevice create(ILocalizationProvider localizationProvider, CollectorCollection<Mode> modes,
                                            IDevice device) {
        SynchronizedDevice result = new SynchronizedDevice();
        setDescriptiveName(device, result);
        result.setIconName(device.getIconName());
        result.setCategory(device.getCategory().toInt());
        result.setControlType(device.getControlType().toInt());
        if (device instanceof IMultiroomDevice) {
            result.setRoomId(((IMultiroomDevice) device).getRoomsIds());
        } else if (device instanceof IRoomDevice) {
            result.setRoomId(((IRoomDevice) device).getRoomId());
        }

        Map<String, SynchronizedState> pStates = new HashMap<>();
        if (device instanceof MultistateDevice) {
            MultistateDevice multistateDevice = (MultistateDevice)device;
            result.setHasNonReadonlyStated(multistateDevice.hasNonReadonlyStates());
            CollectorCollection<State> states = multistateDevice.buildStates(localizationProvider);
            for (State state : states.values()) {
                pStates.put(state.getName().getUniqueId(), IntegrationFactory.create(state));
            }
        }

        if (device instanceof Multicontroller) {
            Multicontroller multicontroller = (Multicontroller)device;
            result.setInitialValue(multicontroller.getInitialValue());
            result.setMinValue(multicontroller.getMinValue());
            result.setMaxValue(multicontroller.getMaxValue());
            result.setUnit("°C");

            Map<String, SynchronizedNamedObject> synchronizedModes = new HashMap<>();
            if (multicontroller.getModesIds() != null && !multicontroller.getModesIds().isEmpty()) {
                String[] modesIds =  multicontroller.getModesIds().split(",");
                for (String modeId : modesIds) {
                    Mode modeToAdd = modes.get(modeId);
                    synchronizedModes.put(modeId, IntegrationFactory.create(modeToAdd));
                }
            }

            Mode defaultMode = new Mode(new DescriptiveName(localizationProvider.getValue("C:Default"), "default"), 0);
            synchronizedModes.put(defaultMode.getName().getUniqueId(), IntegrationFactory.create(defaultMode));
            result.setModes(synchronizedModes);
        }

        result.setStates(pStates);
        return result;
    }

    public static SynchronizedState create(State state) {
        SynchronizedState result = new SynchronizedState();
        setDescriptiveName(state, result);
        result.setCodeRequired(state.isCodeRequired());
        result.setType(state.getType().toInt());
        return result;
    }

    public static SynchronizedEvaluationResult create(IDeviceAutomationUnit unit, AutomationSettings automationSettings) throws ObjectNotFoundException {
        EvaluationResult evaluationResult = unit.buildEvaluationResult();
        SynchronizedEvaluationResult result = new SynchronizedEvaluationResult();
        result.setValue(evaluationResult.getValue().toString());
        result.setInterfaceValue(evaluationResult.getInterfaceValue());
        result.setAuto(evaluationResult.getControlMode() == ControlMode.Auto);
        result.setSignaled(evaluationResult.isSignaled());
        result.setDeviceId(unit.getName().getUniqueId());

        ArrayList<SynchronizedDescription> pDescriptions = new ArrayList<>();
        if (evaluationResult.getDescriptions() != null) {
            for (KeyValue keyValue : evaluationResult.getDescriptions()) {
                pDescriptions.add(IntegrationFactory.create(keyValue));
            }
        }
        result.setDescriptions(pDescriptions);

        if (unit.getDevice() instanceof Multicontroller) {
            result.setModeValues(extractModeValues((Multicontroller)unit.getDevice(), automationSettings));
        }

        return result;
    }

    private static Hashtable<String, Double> extractModeValues(Multicontroller multicontroller, AutomationSettings automationSettings) throws ObjectNotFoundException {
        Hashtable<String, Double> result = new Hashtable<>();
        if (multicontroller.getModesIds() != null && !multicontroller.getModesIds().isEmpty()) {
            String[] modesIds =  multicontroller.getModesIds().split(",");
            for (String modeId : modesIds) {
                String settingsKey = multicontroller.getName().getUniqueId() + "_" + modeId;
                String settingsValue = automationSettings.readSetting(settingsKey, null);
                double modeValue = settingsValue == null ? multicontroller.getInitialValue() : Double.valueOf(settingsValue);
                result.put(modeId, modeValue);
            }
        }

        String defaultSettingsKey = multicontroller.getName().getUniqueId();
        String defaultSettingsValue = automationSettings.readSetting(defaultSettingsKey, null);
        double defaultModeValue = (defaultSettingsValue == null) ? multicontroller.getInitialValue() : Double.valueOf(defaultSettingsValue);
        result.put("default", defaultModeValue);
        return result;
    }

    public static SynchronizedDescription create(KeyValue keyValue) {
        SynchronizedDescription result = new SynchronizedDescription();
        result.setKey(keyValue.getKey());
        result.setValue(keyValue.getValue());
        return result;
    }
}