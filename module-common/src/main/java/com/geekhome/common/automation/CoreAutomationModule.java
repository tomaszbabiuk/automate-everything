package com.geekhome.common.automation;

import com.geekhome.common.configuration.*;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.synchronizationmodule.SmartEvent;

public class CoreAutomationModule implements IAutomationModule {
    private ILogger _logger = LoggingService.getLogger();
    private SystemInfo _systemInfo;
    private MasterAutomation _masterAutomation;
    private MasterConfiguration _masterConfiguration;
    private ILocalizationProvider _localizationProvider;
    private IHardwareManager _hardwareManager;

    public CoreAutomationModule(ILocalizationProvider localizationProvider, IHardwareManager hardwareManager, SystemInfo systemInfo,
                                MasterAutomation masterAutomation, MasterConfiguration masterConfiguration) {
        _systemInfo = systemInfo;
        _masterAutomation = masterAutomation;
        _masterConfiguration = masterConfiguration;
        _localizationProvider = localizationProvider;
        _hardwareManager = hardwareManager;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (DateCondition dateCondition : _masterConfiguration.getDateConditions().values()) {
            DateConditionAutomationUnit unit = new DateConditionAutomationUnit(dateCondition, _localizationProvider);
            conditionsList.add(unit);
        }

        for (MultistateCondition condition : _masterConfiguration.getMultistateConditions().values()) {
            MultistateConditionAutomationUnit unit = new MultistateConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }

        for (TimeCondition condition : _masterConfiguration.getTimeConditions().values()) {
            TimeConditionAutomationUnit unit = new TimeConditionAutomationUnit(condition);
            conditionsList.add(unit);
        }

        for (GeofenceCondition condition : _masterConfiguration.getGeofenceConditions().values()) {
            GeofenceConditionAutomationUnit unit = new GeofenceConditionAutomationUnit(condition);
            conditionsList.add(unit);
        }

        for (ValueCondition condition : _masterConfiguration.getValueConditions().values()) {
            ValueConditionAutomationUnit unit = new ValueConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }

        for (DeltaCondition condition : _masterConfiguration.getDeltaConditions().values()) {
            DeltaConditionAutomationUnit unit = new DeltaConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (GroupCondition condition : _masterConfiguration.getGroupConditions().values()) {
            GroupConditionAutomationUnit unit = new GroupConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (KeySwitch keySwitch : _masterConfiguration.getKeySwitches().values()) {
            DeviceAutomationUnit unit;
            if ((keySwitch.getPortId() != null) && (!keySwitch.getPortId().equals("-"))) {
                IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(keySwitch.getPortId());
                unit = new KeySwitchAutomationUnit(inputPort, keySwitch, _localizationProvider);
            } else {
                unit = new VirtualKeySwitchAutomationUnit(keySwitch, _localizationProvider);
            }
            devicesList.add(unit);
        }
    }

    @Override
    public void addModeAutomationUnits(CollectorCollection<ModeAutomationUnit> modesList) {
        for (Mode mode : _masterConfiguration.getModes().values()) {
            ModeAutomationUnit modeAutomationUnit = new ModeAutomationUnit(mode);
            modesList.put(modeAutomationUnit.getName().getUniqueId(), modeAutomationUnit);
        }
    }

    @Override
    public void addAlertAutomationUnits(CollectorCollection<AlertAutomationUnit> alertsList) {
        for (Alert alert : _masterConfiguration.getAlerts().values()) {
            AlertAutomationUnit alertAutomationUnit = new AlertAutomationUnit(alert, _systemInfo);
            alertsList.put(alertAutomationUnit.getName().getUniqueId(), alertAutomationUnit);
        }
    }

    @Override
    public void addBlocksAutomationUnits(CollectorCollection<BlockAutomationUnit> blocksList) {
        for (Block block : _masterConfiguration.getBlocks().values()) {
            BlockAutomationUnit blockAutomationUnit = new BlockAutomationUnit(block, _masterAutomation);
            blocksList.add(blockAutomationUnit);
        }
    }

    @Override
    public void initialized() {
    }

    @Override
    public void reportSmartEvent(SmartEvent event, String value, String code) throws Exception {
        if (event.equals(SmartEvent.GeofenceEnter) || event.equals(SmartEvent.GeofenceExit)) {
            _logger.info(String.format("Geofence update %s, %s %s", event.toString(), value, code));
            for (GeofenceCondition geofenceCondition : _masterConfiguration.getGeofenceConditions().values()) {
                String id = geofenceCondition.getName().getUniqueId();
                GeofenceConditionAutomationUnit unit = (GeofenceConditionAutomationUnit) _masterAutomation.findConditionUnit(id);
                if (event.equals(SmartEvent.GeofenceEnter)) {
                    unit.geofenceEnter(value);
                } else {
                    unit.geofenceExit(value);
                }
            }
        }
    }
}