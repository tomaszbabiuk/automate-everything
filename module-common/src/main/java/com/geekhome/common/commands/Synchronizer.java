package com.geekhome.common.commands;

import com.geekhome.common.configuration.*;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.synchronizationmodule.*;
import com.geekhome.common.automation.*;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.synchronizationmodule.business.*;

import java.util.*;
import java.util.stream.Collectors;

public class Synchronizer {

    private MasterConfiguration _masterConfiguration;
    private MasterAutomation _masterAutomation;
    private AutomationSettings _automationSettings;
    private ILocalizationProvider _localizationProvider;
    private SystemInfo _systemInfo;
    private CommandsProcessor _commandsProcessor;
    private DashboardAlertService _dashboardAlertService;

    public Synchronizer(MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                        AutomationSettings automationSettings, ILocalizationProvider localizationProvider,
                        SystemInfo systemInfo, CommandsProcessor commandsProcessor, DashboardAlertService dashboardAlertService) {
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _automationSettings = automationSettings;
        _localizationProvider = localizationProvider;
        _systemInfo = systemInfo;
        _commandsProcessor = commandsProcessor;
        _dashboardAlertService = dashboardAlertService;
    }

    public Object processSynchronizationCommand(ReceivedSynchronizationRequest command) throws Exception {
        if (command.getType().equals(SynchronizationCommandType.RefreshFloors)) {
            return synchronizeFloors();
        } else if (command.getType().equals(SynchronizationCommandType.RefreshDevices)) {
            return synchronizeDevices();
        } else if (command.getType().equals(SynchronizationCommandType.RefreshEvaluations)) {
            return synchronizeEvaluations(command.getParameter());
        } else if (command.getType().equals(SynchronizationCommandType.RefreshGeofences)) {
            return synchronizeGeofences();
        } else if (command.getType().equals(SynchronizationCommandType.ControlDevice)) {
            return controlDevice(command);
        } else if (command.getType().equals(SynchronizationCommandType.RegisterDevice)) {
            return registerDevice(command);
        } else if (command.getType().equals(SynchronizationCommandType.ExecuteCommand)) {
            return executeCommand(command);
        } else if (command.getType().equals(SynchronizationCommandType.ReportSmartEvent)) {
            return reportSmartEvent(command);
        } else if (command.getType().equals(SynchronizationCommandType.Ping)) {
            return true;
        } else if (command.getType().equals(SynchronizationCommandType.GetSystemInfo)) {
            return synchronizeSystemInfo();
        }

        return null;
    }

    private Object reportSmartEvent(SynchronizationRequest command) throws Exception {
        SmartEvent event = SmartEvent.fromString(command.getParameter());
        _masterAutomation.reportSmartEvent(event, command.getValue(), command.getCode());
        return new SmartEventResult(true);
    }

    public SynchronizedSystemInfo synchronizeSystemInfo() {
        CollectorCollection<Alert> alerts = _dashboardAlertService.getAlerts();
        return IntegrationFactory.create(_systemInfo, _masterConfiguration, alerts);
    }

    private Object executeCommand(ReceivedSynchronizationRequest command) throws Exception {
        String commandText = command.getCode() == null || command.getCode().isEmpty() ?
                command.getParameter() :
                command.getParameter() + "/" + command.getCode();
        CommandResult processedCommand = _commandsProcessor.processCommand(new Date(), command.getFrom(), commandText, false);
        return IntegrationFactory.create(_automationSettings, _localizationProvider, _masterConfiguration.getModes(), processedCommand);
    }

    private Object registerDevice(SynchronizationRequest command) {
        String millis = String.valueOf(Calendar.getInstance().getTimeInMillis());
        _automationSettings.changeSetting(AutomationSettings.TABLE_DEVICES, command.getValue(), millis);
        return new Object();
    }

    private ArrayList<SynchronizedEvaluationResult> controlDevice(ReceivedSynchronizationRequest command) throws Exception {
        ArrayList<SynchronizedEvaluationResult> result = new ArrayList<>();
        IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(command.getParameter());
        if (deviceUnit instanceof MultistateDeviceAutomationUnit) {
            IMultistateDeviceAutomationUnit multistateUnit = ((MultistateDeviceAutomationUnit) deviceUnit);
            if (command.getValue().equals("auto")) {
                multistateUnit.setControlMode(ControlMode.Auto);
            } else {
                multistateUnit.changeState(command.getValue(), ControlMode.Manual, command.getCode(), command.getFrom());
            }
        }
        if (deviceUnit.getDevice() instanceof Multicontroller) {
            String settingsKey = command.getParameter();
            if (!command.getCode().equals("default")) {
                settingsKey += "_" + command.getCode();
            }
            _automationSettings.changeSetting(settingsKey, command.getValue());
        }

        recalculateDeviceState(deviceUnit);

        result.add(IntegrationFactory.create(deviceUnit, _automationSettings));
        return result;
    }

    private void recalculateDeviceState(IDeviceAutomationUnit deviceUnit) throws Exception {
        if (_systemInfo.getOperationMode() == OperationMode.Automatic && deviceUnit != null) {
            deviceUnit.calculate(Calendar.getInstance());
        }
    }

    public Map<String, SynchronizedEvaluationResult> synchronizeEvaluations(Iterable<String> devicesIdsCommaSeparated) throws Exception {
        Map<String, SynchronizedEvaluationResult> result = new HashMap<>();
        for (String deviceId : devicesIdsCommaSeparated) {
            IDeviceAutomationUnit unit = _masterAutomation.findDeviceUnit(deviceId);
            SynchronizedEvaluationResult evaluationResult = IntegrationFactory.create(unit, _automationSettings);
            result.put(unit.getDevice().getName().getUniqueId(), evaluationResult);
        }

        return result;
    }

    public Map<String, SynchronizedEvaluationResult> synchronizeAllEvaluations() throws Exception {
        Map<String, SynchronizedEvaluationResult> result = new HashMap<>();
        for (IDeviceAutomationUnit unit : _masterAutomation.getDevices().values()) {
            SynchronizedEvaluationResult evaluationResult = IntegrationFactory.create(unit, _automationSettings);
            result.put(unit.getDevice().getName().getUniqueId(), evaluationResult);
        }

        return result;
    }

    public Map<String, SynchronizedEvaluationResult> synchronizeEvaluations(String parameter) throws Exception {
        String[] devicesIds = parameter.split(",");
        Iterable<String> devicesIdsIterable = Arrays.stream(devicesIds).collect(Collectors.toList());
        return synchronizeEvaluations(devicesIdsIterable);
    }

    public SynchronizedEvaluationResult synchronizeEvaluation(String deviceId) throws Exception {
        IDeviceAutomationUnit unit = _masterAutomation.findDeviceUnit(deviceId);
        SynchronizedEvaluationResult evaluationResult = IntegrationFactory.create(unit, _automationSettings);
        return evaluationResult;
    }

    public HashMap<String, SynchronizedDevice> synchronizeDevices() {
        HashMap<String, SynchronizedDevice> result = new HashMap<>();

        CollectorCollection<Mode> modes = _masterConfiguration.getModes();
        for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
            for (Object current : collection.values()) {
                if (current instanceof IDevice) {
                    SynchronizedDevice synchronizedDevice = IntegrationFactory.create(_localizationProvider, modes, (IDevice) current);
                    result.put(synchronizedDevice.getUniqueId(), synchronizedDevice);
                }
            }
        }

        return result;
    }

    public Map<String, SynchronizedFloor> synchronizeFloors() {
        Map<String, SynchronizedFloor> result = new HashMap<>();
        for (Floor floor : _masterConfiguration.getFloors().values()) {
            SynchronizedFloor synchronizedFloor = IntegrationFactory.create(floor);
            result.put(synchronizedFloor.getUniqueId(), synchronizedFloor);
        }
        return result;
    }

    public Map<String, SynchronizedGeofence> synchronizeGeofences() {
        Map<String, SynchronizedGeofence> result = new HashMap<>();
        for (Geofence geofence : _masterConfiguration.getGeofences().values()) {
            SynchronizedGeofence synchronizedGeofence = IntegrationFactory.create(geofence);
            result.put(synchronizedGeofence.getUniqueId(), synchronizedGeofence);
        }
        return result;
    }
}