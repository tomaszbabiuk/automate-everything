package com.geekhome.common.automation;

import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.synchronizationmodule.SmartEvent;

public interface IAutomationModule {
    void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception;

    void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception;

    void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception;

    void addModeAutomationUnits(CollectorCollection<ModeAutomationUnit> modesList);

    void addAlertAutomationUnits(CollectorCollection<AlertAutomationUnit> alertsList);

    void addBlocksAutomationUnits(CollectorCollection<BlockAutomationUnit> blocksList);

    void initialized() throws Exception;

    void reportSmartEvent(SmartEvent event, String value, String code) throws Exception;
}