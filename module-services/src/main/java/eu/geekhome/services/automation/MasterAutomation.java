package eu.geekhome.services.automation;

import java.util.Map;

public class MasterAutomation {
    public Map<String, IDeviceAutomationUnit> getDevices() {
        return null;
    }

    public Map<String, BlockAutomationUnit> getBlocks() {
        return null;
    }

    public IDeviceAutomationUnit findDeviceUnit(String targetId, boolean throwExceptionIfNotFound) {
        return null;
    }

    public IBlocksTargetAutomationUnit findBlockTargetAutomationUnit(String id) {
        return null;
    }

    public Map<String, IEvaluableAutomationUnit> getModes() {
        return null;
    }

    public Map<String, IEvaluableAutomationUnit> getAlerts() {
        return null;
    }

    public IEvaluableAutomationUnit findConditionUnit(String conditionId) {
        return null;
    }
}
