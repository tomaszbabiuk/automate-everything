package com.geekhome.common.automation;

import com.geekhome.common.*;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.hardwaremanager.*;
import com.geekhome.common.OperationMode;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.synchronizationmodule.SmartEvent;

import java.util.ArrayList;
import java.util.Calendar;

public final class MasterAutomation {

    private static final ILogger _logger = LoggingService.getLogger();
    private final AutomationCycler _automationCycler;
    private final SystemInfo _systemInfo;
    private final MasterConfiguration _masterConfiguration;
    private final IHardwareManager _hardwareManager;
    private final DashboardAlertService _dashboardAlertService;
    private final CollectorCollection<IDeviceAutomationUnit> _devices = new CollectorCollection<>();
    private final CollectorCollection<IEvaluableAutomationUnit> _conditions = new CollectorCollection<>();
    private final CollectorCollection<ModeAutomationUnit> _modes = new CollectorCollection<>();
    private final CollectorCollection<BlockAutomationUnit> _blocks = new CollectorCollection<>();
    private ArrayList<IAutomationModule> _modules;
    private ArrayList<IAutomationHook> _hooks;
    private final CollectorCollection<AlertAutomationUnit> _alerts = new CollectorCollection<>();

    public CollectorCollection<IDeviceAutomationUnit> getDevices() {
        return _devices;
    }

    public CollectorCollection<IEvaluableAutomationUnit> getConditions() {
        return _conditions;
    }

    public CollectorCollection<ModeAutomationUnit> getModes() {
        return _modes;
    }

    public CollectorCollection<AlertAutomationUnit> getAlerts() {
        return _alerts;
    }

    public CollectorCollection<BlockAutomationUnit> getBlocks() {
        return _blocks;
    }

    public ArrayList<IAutomationModule> getModules() {
        return _modules;
    }

    public MasterAutomation(MasterConfiguration masterConfiguration, IHardwareManager hardwareManager,
                            SystemInfo systemInfo, DashboardAlertService dashboardAlertService) {
        _systemInfo = systemInfo;
        _masterConfiguration = masterConfiguration;
        _hardwareManager = hardwareManager;
        _dashboardAlertService = dashboardAlertService;
        _automationCycler = new AutomationCycler(this, _systemInfo);
    }

    private boolean reInitialize(boolean allowShadows) throws Exception {
        _logger.debug("Re-Initializing automation");

        getDevices().clear();
        getConditions().clear();
        getModes().clear();
        getAlerts().clear();
        getBlocks().clear();

        if (_modules != null) {
            _masterConfiguration.createDynamicObjectsForAllCollectors();

            try {
                _logger.debug("Automation reinitialization - creating device automation units");
                for (IAutomationModule module : _modules) {
                    module.addDeviceAutomationUnits(getDevices());
                }

                _logger.debug("Automation reinitialization - creating independent condition automation units");
                for (IAutomationModule module : _modules) {
                    module.addIndependentConditionAutomationUnits(getConditions());
                }

                _logger.debug("Automation reinitialization - creating dependent condition automation units");
                for (IAutomationModule module : _modules) {
                    module.addDependentConditionAutomationUnits(getConditions());
                }

                _logger.debug("Automation reinitialization - creating block automation units");
                for (IAutomationModule module : _modules) {
                    module.addBlocksAutomationUnits(getBlocks());
                }

                _logger.debug("Automation reinitialization - creating mode automation units");
                for (IAutomationModule module : _modules) {
                    module.addModeAutomationUnits(getModes());
                }

                _logger.debug("Automation reinitialization - initializing modules");
                for (IAutomationModule module : _modules) {
                    module.initialized();
                }

                _logger.debug("Automation reinitialization - creating alerts");
                for (IAutomationModule module : _modules) {
                    module.addAlertAutomationUnits(getAlerts());
                }
                _logger.info("Automation reinitialized");

                return true;
            } catch (PortNotFoundException ex) {
                if (allowShadows) {
                    addShadowPort(ex.getUniqueId(), ex.getType());
                } else {
                    throw ex;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private void addShadowPort(String portId, PortType type) {
        _logger.info("Adding shadow port for " + portId + " of type " + type);

        switch (type) {
            case DigitalInput: {
                IInputPort<Boolean> base = new SynchronizedInputPort<>(portId, 0);
                IInputPort<Boolean> shadow = new ShadowInputPort<>(portId, base);
                _hardwareManager.getDigitalInputPorts().add(shadow);
                break;
            }
            case DigitalOutput: {
                IOutputPort<Boolean> base = new SynchronizedOutputPort<>(portId, false, 0);
                IOutputPort<Boolean> shadow = new ShadowOutputPort<>(portId, base);
                _hardwareManager.getDigitalOutputPorts().add(shadow);
                break;
            }
            case PowerInput: {
                IInputPort<Double> base = new SynchronizedInputPort<>(portId, 0);
                IInputPort<Double> shadow = new ShadowInputPort<>(portId, base);
                _hardwareManager.getPowerInputPorts().add(shadow);
                break;
            }
            case PowerOutput: {
                IOutputPort<Integer> base = new SynchronizedOutputPort<>(portId, 0, 0);
                IOutputPort<Integer> shadow = new ShadowOutputPort<>(portId, base);
                _hardwareManager.getPowerOutputPorts().add(shadow);
                break;
            }
            case Temperature: {
                IInputPort<Double> base = new SynchronizedInputPort<>(portId, 0);
                IInputPort<Double> shadow = new ShadowInputPort<>(portId, base);
                _hardwareManager.getTemperaturePorts().add(shadow);
                break;
            }
            case Luminosity: {
                IInputPort<Double> base = new SynchronizedInputPort<>(portId, 0);
                IInputPort<Double> shadow = new ShadowInputPort<>(portId, base);
                _hardwareManager.getLuminosityPorts().add(shadow);
                break;
            }
            case Humidity: {
                IInputPort<Double> base = new SynchronizedInputPort<>(portId, 0);
                IInputPort<Double> shadow = new ShadowInputPort<>(portId, base);
                _hardwareManager.getHumidityPorts().add(shadow);
                break;
            }
            case Toggle: {
                ITogglePort base = new SynchronizedTogglePort(portId, 0);
                ITogglePort shadow = new ShadowTogglePort<>(portId, base);
                _hardwareManager.getTogglePorts().add(shadow);
                break;
            }
        }

        raiseShadowPortsUsedAlert();
    }

    private void raiseShadowPortsUsedAlert() {
        _dashboardAlertService.raiseShadowPortsInUse();
        _hardwareManager.scheduleRediscovery();
    }

    private void stopAllDevices() throws Exception {
        _logger.info("Stopping all devices");
        for (IOutputPort<Boolean> outputPort : _hardwareManager.getDigitalOutputPorts().values()) {
            if (outputPort.getMappedTo().size() > 0) {
                outputPort.write(false);
            }
        }
        for (ITogglePort togglePort : _hardwareManager.getTogglePorts().values()) {
            if (togglePort.getMappedTo().size() > 0) {
                togglePort.toggleOff();
            }
        }
    }

    public void systemInfoChanged(OperationMode newOperationMode) throws Exception {
        stopAllDevices();
        _dashboardAlertService.clearAllAlerts();
        _automationCycler.setAutomationEnabled(false);

        if (newOperationMode == OperationMode.Automatic) {
            try {
                if (!reInitialize(true)) {
                    reInitialize(false);
                }
                _automationCycler.setAutomationEnabled(true);
            } catch (Exception ex) {
                if (_systemInfo.getOperationMode() == OperationMode.Automatic) {
                    _systemInfo.setOperationMode(OperationMode.Diagnostics);
                    _systemInfo.scheduleAutomaticRestart();
                    _logger.error("Error starting automation... fallback to diagnostic mode", ex);
                }
            }
        }
        _hardwareManager.invalidateAdapters(newOperationMode);

        triggerBeforeAutomationHooks();
    }

    public static void addByPriority(ArrayList<ModeAutomationUnit> modes, ModeAutomationUnit mode) {
        for (int i = 0; i < modes.size(); i++) {
            IPrioritized iPrioritized = modes.get(i);
            if (mode.getPriority() >= iPrioritized.getPriority()) {
                modes.add(i, mode);
                return;
            }
        }

        modes.add(modes.size(), mode);
    }

    public IEvaluableAutomationUnit findConditionUnit(String conditionId, boolean throwExceptionIfNotFound) throws Exception {
        if (getConditions().containsKey(conditionId)) {
            return getConditions().get(conditionId);
        } else {
            if (throwExceptionIfNotFound) {
                throw new Exception("Cannot find condition unit: " + conditionId);
            }

            return null;
        }
    }

    public IEvaluableAutomationUnit findConditionUnit(String conditionId) throws Exception {
        return findConditionUnit(conditionId, true);
    }

    public IDeviceAutomationUnit findDeviceUnit(String deviceId, boolean throwExceptionIfNotFound) throws Exception {
        return getDevices().find(deviceId, throwExceptionIfNotFound);
    }

    public IDeviceAutomationUnit findDeviceUnit(String deviceId) throws Exception {
        return findDeviceUnit(deviceId, true);
    }

    public ModeAutomationUnit findModeUnit(String modeId, boolean throwExceptionIfNotFound) throws Exception {
        return getModes().find(modeId, throwExceptionIfNotFound);
    }

    public ModeAutomationUnit findModeUnit(String modeId) throws Exception {
        return findModeUnit(modeId, true);
    }

    public AlertAutomationUnit findAlertUnit(String alertId, boolean throwExceptionIfNotFound) throws Exception {
        return getAlerts().find(alertId, throwExceptionIfNotFound);
    }

    public AlertAutomationUnit findAlertUnit(String modeId) throws Exception {
        return findAlertUnit(modeId, true);
    }

    public IBlocksTargetAutomationUnit findBlockTargetAutomationUnit(String id) throws Exception {
        IBlocksTargetAutomationUnit modeUnit = findModeUnit(id, false);
        if (modeUnit != null) {
            return modeUnit;
        } else {
            return findAlertUnit(id);
        }
    }

    public BlockAutomationUnit findBlockUnit(String blockId, boolean throwExceptionIfNotFound) throws Exception {
        return getBlocks().find(blockId, throwExceptionIfNotFound);
    }

    public IEvaluableAutomationUnit findBlockUnit(String blockId) throws Exception {
        return findBlockUnit(blockId, true);
    }

    private void setAutomationModules(ArrayList<IAutomationModule> automationModules) {
        _modules = automationModules;
    }

    public void startAutomationLoop() {
        _logger.info("Starting automation loop");
        triggerBeforeAutomationHooks();
        while (true) {
            Calendar now = Calendar.getInstance();
            try {
                _hardwareManager.refreshAndWait(now);
                _automationCycler.automate(now, this::triggerAfterAutomationHooks);
            } catch (Exception ex) {
                if (_systemInfo.getOperationMode() == OperationMode.Automatic) {
                    _logger.error("Unhandled exception during automation cycle... fallback to diagnostic mode", ex);
                    try {
                        _systemInfo.setOperationMode(OperationMode.Diagnostics);
                        _systemInfo.scheduleAutomaticRestart();
                    } catch (Exception e1) {
                        _logger.error("Cannot fallback to diagnostic mode!", ex);
                    }
                }
            }

            _systemInfo.doAutomaticRestartIfScheduled(now);
            _hardwareManager.doRediscoveryIfScheduled(now);
            _hardwareManager.scheduleRediscoveryIfUsingShadows(now);
            Sleeper.trySleep(100);
        }
    }

    private void triggerAfterAutomationHooks() {
        for (IAutomationHook hook : _hooks) {
            hook.onAutomationLoop();
        }
    }

    private void triggerBeforeAutomationHooks() {
        for (IAutomationHook hook : _hooks) {
            hook.beforeAutomationLoop();
        }
    }

    public void initialize(ArrayList<IAutomationModule> automationModules, ArrayList<IAutomationHook> automationHooks) {
        setAutomationModules(automationModules);
        setAutomationHooks(automationHooks);
    }

    private void setAutomationHooks(ArrayList<IAutomationHook> automationHooks) {
        _hooks = automationHooks;
    }

    public void reportSmartEvent(SmartEvent event, String value, String code) throws Exception {
        for (IAutomationModule module : _modules) {
            module.reportSmartEvent(event, value, code);
        }
    }
}